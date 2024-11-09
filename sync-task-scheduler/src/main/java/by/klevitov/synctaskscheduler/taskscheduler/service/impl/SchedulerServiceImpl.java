package by.klevitov.synctaskscheduler.taskscheduler.service.impl;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus;
import by.klevitov.synctaskscheduler.taskscheduler.exception.SchedulerServiceException;
import by.klevitov.synctaskscheduler.taskscheduler.service.SchedulerService;
import by.klevitov.synctaskscheduler.taskscheduler.service.TaskService;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.DELETION_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.PAUSING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.RESCHEDULING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.RESUMING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.SCHEDULING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.TRIGGERING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.JOB_GROUP;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.createJobDetail;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.createTrigger;

@Log4j2
@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final Scheduler scheduler;
    private final TaskService taskService;

    @Autowired
    public SchedulerServiceImpl(Scheduler scheduler, TaskService taskService) {
        this.scheduler = scheduler;
        this.taskService = taskService;
    }

    @Override
    public Task scheduleTask(Task task) {
        JobDetail jobDetail = createJobDetail(task);
        Trigger trigger = createTrigger(jobDetail, task);
        scheduleJob(jobDetail, trigger);
        return task;
    }

    @Override
    public List<Task> scheduleTask(List<Task> tasks) {
        for (Task task : tasks) {
            JobDetail jobDetail = createJobDetail(task);
            Trigger trigger = createTrigger(jobDetail, task);
            scheduleJob(jobDetail, trigger);
        }
        return tasks;
    }

    private void scheduleJob(final JobDetail jobDetail, final Trigger trigger) {
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(SCHEDULING_JOB_ERROR, jobDetail.getKey());
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }

    @Override
    public Task rescheduleTask(Task task) {
        JobDetail jobDetail = createJobDetail(task);
        Trigger trigger = createTrigger(jobDetail, task);
        rescheduleJob(TriggerKey.triggerKey(task.createTriggerIdentityName()), trigger);
        return task;
    }

    private void rescheduleJob(final TriggerKey triggerKey, final Trigger trigger) {
        try {
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(RESCHEDULING_JOB_ERROR, triggerKey);
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }

    @Override
    public Task pauseTask(Task task) {
        JobKey key = JobKey.jobKey(task.createTaskIdentityName(), JOB_GROUP);
        pauseJob(key);
        return taskService.updateStatus(task.getId(), TaskStatus.PAUSED);
    }

    private void pauseJob(final JobKey jobKey) {
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(PAUSING_JOB_ERROR, jobKey);
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }

    @Override
    public Task resumeTask(Task task) {
        JobKey key = JobKey.jobKey(task.createTaskIdentityName(), JOB_GROUP);
        resumeJob(key);
        return taskService.updateStatus(task.getId(), TaskStatus.ACTIVE);
    }

    private void resumeJob(final JobKey jobKey) {
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(RESUMING_JOB_ERROR, jobKey);
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }

    @Override
    public boolean deleteTask(Task task) {
        JobKey key = JobKey.jobKey(task.createTaskIdentityName(), JOB_GROUP);
        return deleteJob(key);
    }

    @Override
    public void triggerTask(Task task) {
        JobKey key = JobKey.jobKey(task.createTaskIdentityName());
        try {
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(TRIGGERING_JOB_ERROR, key);
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }

    private boolean deleteJob(final JobKey jobKey) {
        try {
            return scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(DELETION_JOB_ERROR, jobKey);
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }
}
