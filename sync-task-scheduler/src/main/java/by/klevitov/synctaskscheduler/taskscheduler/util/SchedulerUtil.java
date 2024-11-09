package by.klevitov.synctaskscheduler.taskscheduler.util;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.job.MessageSendingJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public final class SchedulerUtil {
    public static final String JOB_GROUP = "sync_j";
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
}
