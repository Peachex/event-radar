package by.klevitov.synctaskscheduler.taskscheduler.quartz.trigger;

import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.Date;

public class PauseAwareCronTrigger extends CronTriggerImpl {
    public PauseAwareCronTrigger(TriggerKey key, CronExpression cronExpression, JobKey jobKey) {
        super();
        super.setKey(key);
        super.setCronExpression(cronExpression);
        super.setJobKey(jobKey);
    }

    @Override
    public Date getNextFireTime() {
        Date nextFireTime = super.getNextFireTime();
        if (nextFireTimeHasExpired(nextFireTime)) {
            nextFireTime = super.getFireTimeAfter(null);
            super.setNextFireTime(nextFireTime);
        }
        return nextFireTime;
    }

    private boolean nextFireTimeHasExpired(final Date nextFireTime) {
        return (nextFireTime.getTime() < System.currentTimeMillis());
    }
}
