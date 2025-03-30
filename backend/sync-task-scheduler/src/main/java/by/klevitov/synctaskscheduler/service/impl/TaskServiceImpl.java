package by.klevitov.synctaskscheduler.service.impl;

import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.synctaskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.entity.TaskStatus;
import by.klevitov.synctaskscheduler.exception.TaskAlreadyExistsException;
import by.klevitov.synctaskscheduler.exception.TaskNotFoundException;
import by.klevitov.synctaskscheduler.repository.TaskJpaRepository;
import by.klevitov.synctaskscheduler.service.TaskService;
import by.klevitov.synctaskscheduler.util.TaskValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static by.klevitov.eventradarcommon.pagination.util.PageRequestValidator.validatePageRequest;
import static by.klevitov.eventradarcommon.pagination.util.PaginationUtil.trimToPageSize;
import static by.klevitov.synctaskscheduler.constant.TaskSchedulerExceptionMessage.TASK_ALREADY_EXISTS;
import static by.klevitov.synctaskscheduler.constant.TaskSchedulerExceptionMessage.TASK_NOT_FOUND;
import static by.klevitov.synctaskscheduler.util.TaskValidator.throwExceptionInCaseOfNullStatus;
import static by.klevitov.synctaskscheduler.util.TaskValidator.validateTaskBeforeCreation;
import static by.klevitov.synctaskscheduler.util.TaskValidator.validateTaskBeforeUpdating;
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
    public Task create(final Task task) {
        validateTaskBeforeCreation(task);
        return createTaskOrGetExistingOne(task);
    }

    private Task createTaskOrGetExistingOne(final Task task) {
        return repository.findByNameIgnoreCase(task.getName()).orElseGet(() -> repository.save(task));
    }

    @Override
    public List<Task> create(final List<Task> tasks) {
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
    public Task findById(final long id) {
        Optional<Task> task = repository.findById(id);
        return task.orElseThrow(() -> createAndLogTaskNotFoundException(id));
    }

    private TaskNotFoundException createAndLogTaskNotFoundException(final long id) {
        final String exceptionMessage = String.format(TASK_NOT_FOUND, id);
        log.error(exceptionMessage);
        return new TaskNotFoundException(exceptionMessage);
    }

    @Override
    public List<Task> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch) {
        return (isNotEmpty(fields) ? repository.findByFields(fields, isCombinedMatch) : new ArrayList<>());
    }

    @Override
    public Page<Task> findByFields(Map<String, Object> fields, boolean isCombinedMatch, PageRequestDTO pageRequestDTO) {
        validatePageRequest(pageRequestDTO, Task.class);
        return (isNotEmpty(fields)
                ? createPaginationFindByFieldsResult(fields, isCombinedMatch, pageRequestDTO)
                : new PageImpl<>(new ArrayList<>()));
    }

    private Page<Task> createPaginationFindByFieldsResult(Map<String, Object> fields, boolean isCombinedMatch,
                                                          PageRequestDTO pageRequestDTO) {
        List<Task> tasks = repository.findByFields(fields, isCombinedMatch);
        int totalCount = tasks.size();
        trimToPageSize(tasks, pageRequestDTO.getSize());
        return new PageImpl<>(tasks, pageRequestDTO.createPageRequest(), totalCount);
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Task> findAll(PageRequestDTO pageRequestDTO) {
        validatePageRequest(pageRequestDTO, Task.class);
        return repository.findAll(pageRequestDTO.createPageRequest());
    }

    @Override
    public List<Task> findByStatus(final TaskStatus status) {
        throwExceptionInCaseOfNullStatus(status);
        return repository.findByStatus(status);
    }

    @Override
    public Task update(final Task updatedTask) {
        validateTaskBeforeUpdating(updatedTask);
        Task existentTask = findById(updatedTask.getId());
        updatedTask.copyValuesForNullOrEmptyFieldsFromTask(existentTask);
        throwExceptionInCaseOfTaskAlreadyExists(updatedTask);
        return repository.save(updatedTask);
    }

    @Override
    public Task updateStatus(final long id, final TaskStatus updatedStatus) {
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
    public void delete(final long id) {
        findById(id);
        repository.deleteById(id);
    }
}
