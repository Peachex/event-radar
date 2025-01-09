package by.klevitov.synctaskscheduler.util;

import by.klevitov.synctaskscheduler.entity.Task;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobKey;

@Log4j2
public final class SchedulerUtil {
    public static final String JOB_GROUP = "sync_j";

    private SchedulerUtil() {
    }

    public static JobKey createJobKeyBasedOnTask(final Task task) {
        return new JobKey(task.createTaskIdentityName(), JOB_GROUP);
    }
}
