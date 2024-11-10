package by.klevitov.synctaskscheduler.taskscheduler.service.impl;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
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
import static by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus.ACTIVE;
import static by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus.PAUSED;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.createJobDetail;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.createJobKeyBasedOnTask;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.createTrigger;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.createTriggerKeyBasedOnTask;

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
        return (task.getStatus().equals(ACTIVE) ? task : taskService.updateStatus(task.getId(), ACTIVE));
    }

    @Override
    public List<Task> scheduleTask(List<Task> tasks) {
        for (Task task : tasks) {
            JobDetail jobDetail = createJobDetail(task);
            Trigger trigger = createTrigger(jobDetail, task);
            scheduleJob(jobDetail, trigger);
        }
        return retrieveTasksWithUpdatedStatus(tasks);
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
    public Task rescheduleTask(Task task) {
        JobDetail jobDetail = createJobDetail(task);
        Trigger trigger = createTrigger(jobDetail, task);
        rescheduleJob(createTriggerKeyBasedOnTask(task), trigger);
        pauseJobIfTaskWasPaused(task);
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

    private void pauseJobIfTaskWasPaused(final Task task) {
        if (task.getStatus().equals(PAUSED)) {
            pauseTask(task);
        }
    }

    @Override
    public Task pauseTask(Task task) {
        JobKey key = createJobKeyBasedOnTask(task);
        pauseJob(key);
        return taskService.updateStatus(task.getId(), PAUSED);
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
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        return (jobDetail != null);
    }

    @Override
    public boolean deleteTask(Task task) {
        JobKey key = createJobKeyBasedOnTask(task);
        return deleteJob(key);
    }

    @Override
    public void triggerTask(Task task) {
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
