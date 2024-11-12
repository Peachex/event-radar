package by.klevitov.synctaskscheduler.taskscheduler.quartz.creator;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public interface QuartzEntityCreator {
    JobDetail createJobDetail(final Task task);

    Trigger createTrigger(final JobDetail jobDetail, final Task task);
}
