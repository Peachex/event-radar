package by.klevitov.synctaskscheduler.taskscheduler.util;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.job.MessageSendingJob;
import lombok.extern.log4j.Log4j2;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

@Log4j2
public final class SchedulerUtil {
    private static final String JOB_GROUP = "sync_j";
    private static final String TRIGGER_GROUP = "sync_t";
    private static final String TASK_ID_KEY = "taskId";

    private SchedulerUtil() {
    }

    public static JobDetail createJobDetail(final Task task) {
        return JobBuilder.newJob(MessageSendingJob.class)
                .withIdentity(task.createTaskIdentityName(), JOB_GROUP)
                .usingJobData(TASK_ID_KEY, task.getTaskIdToExecute())
                .build();
    }

    public static Trigger createTrigger(final JobDetail jobDetail, final Task task) {
        return TriggerBuilder.newTrigger()
                .withIdentity(task.createTriggerIdentityName(), TRIGGER_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()))
                .forJob(jobDetail)
                .build();
    }

    public static JobKey createJobKeyBasedOnTask(final Task task) {
        return new JobKey(task.createTaskIdentityName(), JOB_GROUP);
    }

    public static TriggerKey createTriggerKeyBasedOnTask(final Task task) {
        return new TriggerKey(task.createTriggerIdentityName(), TRIGGER_GROUP);
    }
}
