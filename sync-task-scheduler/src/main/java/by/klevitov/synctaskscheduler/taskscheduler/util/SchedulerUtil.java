package by.klevitov.synctaskscheduler.taskscheduler.util;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

@Log4j2
public final class SchedulerUtil {
    public static final String JOB_GROUP = "sync_j";
    public static final String TRIGGER_GROUP = "sync_t";

    private SchedulerUtil() {
    }

    public static JobKey createJobKeyBasedOnTask(final Task task) {
        return new JobKey(task.createTaskIdentityName(), JOB_GROUP);
    }

    public static TriggerKey createTriggerKeyBasedOnTask(final Task task) {
        return new TriggerKey(task.createTriggerIdentityName(), TRIGGER_GROUP);
    }
}
