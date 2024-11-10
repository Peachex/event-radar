package by.klevitov.synctaskscheduler.taskscheduler.util;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.job.MessageSendingJob;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.trigger.PauseAwareCronTrigger;
import lombok.extern.log4j.Log4j2;
import org.quartz.CronExpression;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.text.ParseException;

import static by.klevitov.synctaskscheduler.taskscheduler.util.TaskValidator.validateCronExpression;

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
        TriggerKey triggerKey = createTriggerKeyBasedOnTask(task);
        CronExpression cronExpression = createCronExpression(task.getCronExpression());
        JobKey jobKey = jobDetail.getKey();
        return new PauseAwareCronTrigger(triggerKey, cronExpression, jobKey);
    }

    private static CronExpression createCronExpression(String cronExpressionStr) {
        CronExpression cronExpression = null;
        try {
            validateCronExpression(cronExpressionStr);
            cronExpression = new CronExpression(cronExpressionStr);
        } catch (ParseException e) {
            log.error(e);
        }
        return cronExpression;
    }

    public static JobKey createJobKeyBasedOnTask(final Task task) {
        return new JobKey(task.createTaskIdentityName(), JOB_GROUP);
    }

    public static TriggerKey createTriggerKeyBasedOnTask(final Task task) {
        return new TriggerKey(task.createTriggerIdentityName(), TRIGGER_GROUP);
    }
}
