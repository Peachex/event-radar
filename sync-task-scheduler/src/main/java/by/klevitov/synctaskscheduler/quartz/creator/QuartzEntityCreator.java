package by.klevitov.synctaskscheduler.quartz.creator;

import by.klevitov.synctaskscheduler.entity.Task;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public interface QuartzEntityCreator {
    JobDetail createJobDetail(final Task task);

    Trigger createTrigger(final JobDetail jobDetail, final Task task);
}
