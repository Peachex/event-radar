package by.klevitov.synctaskscheduler.taskscheduler.service.impl;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus;
import by.klevitov.synctaskscheduler.taskscheduler.exception.TaskAlreadyExistsException;
import by.klevitov.synctaskscheduler.taskscheduler.exception.TaskNotFoundException;
import by.klevitov.synctaskscheduler.taskscheduler.repository.TaskJpaRepository;
import by.klevitov.synctaskscheduler.taskscheduler.service.TaskService;
import by.klevitov.synctaskscheduler.taskscheduler.util.TaskValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.TASK_ALREADY_EXISTS;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.TASK_NOT_FOUND;
import static by.klevitov.synctaskscheduler.taskscheduler.util.TaskValidator.validateTaskBeforeCreation;
import static by.klevitov.synctaskscheduler.taskscheduler.util.TaskValidator.validateTaskBeforeUpdating;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

@Log4j2
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskJpaRepository repository;

    @Autowired
    public TaskServiceImpl(TaskJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Task create(Task task) {
        validateTaskBeforeCreation(task);
        //todo Add task scheduling.
        return createTaskOrGetExistingOne(task);
    }

    private Task createTaskOrGetExistingOne(final Task task) {
        //todo Add task scheduling.
        return repository.findByNameIgnoreCase(task.getName()).orElseGet(() -> repository.save(task));
    }

    @Override
    public List<Task> create(List<Task> tasks) {
        tasks.forEach(TaskValidator::validateTaskBeforeCreation);
        return createTasksWithoutDuplication(tasks);
    }

    private List<Task> createTasksWithoutDuplication(final List<Task> tasks) {
        List<Task> existentTasks = repository.findAll();
        List<Task> nonExistentTasks = createNonExistentTasksList(tasks, existentTasks);
        existentTasks.addAll(repository.saveAll(nonExistentTasks));
        Map<String, Task> existentTasksWithKey = createTasksMapWithTaskNameKey(existentTasks);
        updateTasksWithId(tasks, existentTasksWithKey);
        return tasks;
    }

    private List<Task> createNonExistentTasksList(final List<Task> tasks, final List<Task> existentTasks) {
        Set<Task> nonExistentTasks = new HashSet<>();
        Map<String, Task> tasksWithKey = createTasksMapWithTaskNameKey(existentTasks);
        tasks.forEach(t -> {
            if (!tasksWithKey.containsKey(t.getName())) {
                nonExistentTasks.add(t);
            }
        });
        return nonExistentTasks.stream().toList();
    }

    private Map<String, Task> createTasksMapWithTaskNameKey(final List<Task> tasks) {
        Map<String, Task> tasksMap = new HashMap<>();
        tasks.forEach(t -> tasksMap.put(t.getName(), t));
        return tasksMap;
    }

    private void updateTasksWithId(final List<Task> tasks, final Map<String, Task> existentTasksWithKey) {
        tasks.forEach(t -> t.setId(existentTasksWithKey.get(t.getName()).getId()));
    }

    @Override
    public Task findById(long id) {
        Optional<Task> task = repository.findById(id);
        return task.orElseThrow(() -> createAndLogTaskNotFoundException(id));
    }

    private TaskNotFoundException createAndLogTaskNotFoundException(final long id) {
        final String exceptionMessage = String.format(TASK_NOT_FOUND, id);
        log.error(exceptionMessage);
        return new TaskNotFoundException(exceptionMessage);
    }

    @Override
    public List<Task> findByFields(Map<String, Object> fields) {
        return (isNotEmpty(fields) ? repository.findByFields(fields) : new ArrayList<>());
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public Task update(Task updatedTask) {
        //todo Reschedule task.
        validateTaskBeforeUpdating(updatedTask);
        Task existentTask = findById(updatedTask.getId());
        updatedTask.copyValuesForNullOrEmptyFieldsFromTask(existentTask);
        throwExceptionInCaseOfTaskAlreadyExists(updatedTask);
        return repository.save(updatedTask);
    }

    @Override
    public Task updateStatus(long id, TaskStatus updatedStatus) {
        Task updatedTask = findById(id);
        updatedTask.setStatus(updatedStatus);
        return repository.save(updatedTask);
    }

    private void throwExceptionInCaseOfTaskAlreadyExists(final Task updatedTask) {
        if (updatedTaskAlreadyExists(updatedTask)) {
            final String exceptionMessage = String.format(TASK_ALREADY_EXISTS, updatedTask.getName(),
                    updatedTask.getId());
            log.error(exceptionMessage);
            throw new TaskAlreadyExistsException(exceptionMessage);
        }
    }

    private boolean updatedTaskAlreadyExists(final Task updatedTask) {
        final Optional<Task> existentTask = repository.findByNameIgnoreCase(updatedTask.getName());
        return (existentTask.isPresent() && existentTask.get().getId() != updatedTask.getId());
    }

    @Override
    public void delete(long id) {
        //todo Remove task from schedule.
        findById(id);
        repository.deleteById(id);
    }
}
