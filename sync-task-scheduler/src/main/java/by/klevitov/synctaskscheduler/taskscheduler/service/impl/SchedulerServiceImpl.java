package by.klevitov.synctaskscheduler.taskscheduler.service.impl;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.exception.SchedulerServiceException;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.QuartzEntityCreator;
import by.klevitov.synctaskscheduler.taskscheduler.service.SchedulerService;
import by.klevitov.synctaskscheduler.taskscheduler.service.TaskService;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.DELETION_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.PAUSING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.RESCHEDULING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.RESUMING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.SCHEDULING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.TRIGGERING_JOB_ERROR;
import static by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus.ACTIVE;
import static by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus.PAUSED;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.createJobKeyBasedOnTask;

@Log4j2
@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final Scheduler scheduler;
    private final QuartzEntityCreator quartzCreator;
    private final TaskService taskService;

    @Autowired
    public SchedulerServiceImpl(Scheduler scheduler, QuartzEntityCreator quartzCreator, TaskService taskService) {
        this.scheduler = scheduler;
        this.quartzCreator = quartzCreator;
        this.taskService = taskService;
    }

    @Override
    public Task scheduleTask(final Task task) {
        JobDetail jobDetail = quartzCreator.createJobDetail(task);
        Trigger trigger = quartzCreator.createTrigger(jobDetail, task);
        scheduleJob(jobDetail, trigger);
        return (task.getStatus().equals(ACTIVE) ? task : taskService.updateStatus(task.getId(), ACTIVE));
    }

    @Override
    public List<Task> scheduleTask(final List<Task> tasks) {
        for (Task task : tasks) {
            JobDetail jobDetail = quartzCreator.createJobDetail(task);
            Trigger trigger = quartzCreator.createTrigger(jobDetail, task);
            scheduleJob(jobDetail, trigger);
        }
        return retrieveTasksWithUpdatedStatus(tasks);
    }

    private void scheduleJob(final JobDetail jobDetail, final Trigger trigger) {
        try {
            if (jobWasNotPreviouslyScheduled(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(SCHEDULING_JOB_ERROR, jobDetail.getKey());
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }

    private boolean jobWasNotPreviouslyScheduled(final JobKey jobKey) throws SchedulerException {
        return !scheduler.checkExists(jobKey);
    }

    private List<Task> retrieveTasksWithUpdatedStatus(final List<Task> tasks) {
        tasks.stream()
                .filter(t -> t.getStatus().equals(PAUSED))
                .forEach(t -> {
                    taskService.updateStatus(t.getId(), ACTIVE);
                    t.setStatus(ACTIVE);
                });
        return tasks;
    }

    @Override
    public Task rescheduleTask(final Task task) {
        JobDetail jobDetail = quartzCreator.createJobDetail(task);
        Trigger trigger = quartzCreator.createTrigger(jobDetail, task);
        rescheduleJob(trigger);
        pauseJobIfTaskWasPaused(task);
        return task;
    }

    private void rescheduleJob(final Trigger trigger) {
        try {
            scheduler.rescheduleJob(trigger.getKey(), trigger);
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(RESCHEDULING_JOB_ERROR, trigger.getKey());
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }

    private void pauseJobIfTaskWasPaused(final Task task) {
        if (task.getStatus().equals(PAUSED)) {
            pauseTask(task);
        }
    }

    @Override
    public Task pauseTask(final Task task) {
        JobKey key = createJobKeyBasedOnTask(task);
        pauseJob(key);
        return task.getStatus().equals(PAUSED) ? task : taskService.updateStatus(task.getId(), PAUSED);
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
    public Task resumeTask(final Task task) {
        resumeJob(task);
        return taskService.updateStatus(task.getId(), ACTIVE);
    }

    private void resumeJob(final Task task) {
        JobKey jobKey = createJobKeyBasedOnTask(task);
        try {
            if (jobWasPreviouslyScheduled(jobKey)) {
                scheduler.resumeJob(jobKey);
            } else {
                scheduleTask(task);
            }
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(RESUMING_JOB_ERROR, jobKey);
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }

    private boolean jobWasPreviouslyScheduled(final JobKey jobKey) throws SchedulerException {
        return scheduler.checkExists(jobKey);
    }

    @Override
    public boolean deleteTask(final Task task) {
        JobKey key = createJobKeyBasedOnTask(task);
        return deleteJob(key);
    }

    @Override
    public void triggerTask(final Task task) {
        JobKey key = createJobKeyBasedOnTask(task);
        try {
            scheduleJobAndPauseIfNeeded(task, key);
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            String exceptionMessage = String.format(TRIGGERING_JOB_ERROR, key);
            log.error(exceptionMessage);
            throw new SchedulerServiceException(exceptionMessage, e);
        }
    }

    private void scheduleJobAndPauseIfNeeded(final Task task, final JobKey jobKey) throws SchedulerException {
        if (!jobWasPreviouslyScheduled(jobKey)) {
            scheduleTask(task);
            pauseTask(task);
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
