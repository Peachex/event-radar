package by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.producer.TaskSchedulerProducer;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.QuartzEntityCreator;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.job.MessageSendingJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.JOB_GROUP;

@Component
public class QuartzEntityCreatorImpl implements QuartzEntityCreator {
    public static final String TASK_ID_TO_EXECUTE_KEY = "taskIdToExecute";
    public static final String PRODUCER_KEY = "taskSchedulerProducer";
    public static final String TRIGGER_GROUP = "sync_t";
    private final TaskSchedulerProducer producer;

    @Autowired
    public QuartzEntityCreatorImpl(TaskSchedulerProducer producer) {
        this.producer = producer;
    }

    @Override
    public JobDetail createJobDetail(final Task task) {
        return JobBuilder.newJob(MessageSendingJob.class)
                .withIdentity(task.createTaskIdentityName(), JOB_GROUP)
                .usingJobData(createJobDataMap(task.getTaskIdToExecute()))
                .build();
    }

    private JobDataMap createJobDataMap(final String taskIdToExecute) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(PRODUCER_KEY, producer);
        jobDataMap.put(TASK_ID_TO_EXECUTE_KEY, taskIdToExecute);
        return jobDataMap;
    }

    //todo Check if there is another way of passing producer to the quartz job.

    @Override
    public Trigger createTrigger(final JobDetail jobDetail, final Task task) {
        return TriggerBuilder.newTrigger()
                .withIdentity(task.createTriggerIdentityName(), TRIGGER_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()))
                .forJob(jobDetail)
                .build();
    }
}
