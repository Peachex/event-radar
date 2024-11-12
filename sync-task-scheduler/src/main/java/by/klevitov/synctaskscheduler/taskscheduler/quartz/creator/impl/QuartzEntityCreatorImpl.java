package by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.QuartzEntityCreator;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.job.MessageSendingJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.JOB_GROUP;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.TRIGGER_GROUP;

@Component
public class QuartzEntityCreatorImpl implements QuartzEntityCreator {
    private static final String TASK_ID_KEY = "taskId";

    @Override
    public JobDetail createJobDetail(final Task task) {
        return JobBuilder.newJob(MessageSendingJob.class)
                .withIdentity(task.createTaskIdentityName(), JOB_GROUP)
                .usingJobData(TASK_ID_KEY, task.getTaskIdToExecute())
                .build();
    }

    @Override
    public Trigger createTrigger(final JobDetail jobDetail, final Task task) {
        return TriggerBuilder.newTrigger()
                .withIdentity(task.createTriggerIdentityName(), TRIGGER_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()))
                .forJob(jobDetail)
                .build();
    }
}
