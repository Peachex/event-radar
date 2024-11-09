package by.klevitov.synctaskscheduler.taskscheduler.service;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;

import java.util.List;

public interface SchedulerService {
    Task scheduleTask(final Task task);

    List<Task> scheduleTask(final List<Task> tasks);

    Task rescheduleTask(final Task task);

    Task pauseTask(final Task task);

    Task resumeTask(final Task task);

    boolean deleteTask(final Task task);

    void triggerTask(final Task task);
}
