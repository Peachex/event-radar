package by.klevitov.synctaskscheduler.taskscheduler.service;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus;

import java.util.List;
import java.util.Map;

public interface TaskService {
    Task create(final Task task);

    List<Task> create(final List<Task> tasks);

    Task findById(final long id);

    List<Task> findByFields(final Map<String, Object> fields);

    List<Task> findAll();

    List<Task> findByStatus(final TaskStatus status);

    Task update(final Task updatedTask);

    Task updateStatus(final long id, final TaskStatus updatedStatus);

    void delete(final long id);
}
