package by.klevitov.synctaskscheduler.service.impl;

import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.eventradarcommon.pagination.util.PageRequestValidator;
import by.klevitov.synctaskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.exception.TaskAlreadyExistsException;
import by.klevitov.synctaskscheduler.exception.TaskNotFoundException;
import by.klevitov.synctaskscheduler.exception.TaskValidatorException;
import by.klevitov.synctaskscheduler.repository.TaskJpaRepository;
import by.klevitov.synctaskscheduler.service.TaskService;
import by.klevitov.synctaskscheduler.util.TaskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.klevitov.synctaskscheduler.entity.TaskStatus.ACTIVE;
import static by.klevitov.synctaskscheduler.entity.TaskStatus.PAUSED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceImplTest {
    private TaskService service;
    private TaskJpaRepository mockedRepository;

    @BeforeEach
    public void setUp() {
        mockedRepository = Mockito.mock(TaskJpaRepository.class);
        service = new TaskServiceImpl(mockedRepository);
    }

    @Test
    public void test_create_withValidUniqueSingleTask() {
        try (MockedStatic<TaskValidator> validator = Mockito.mockStatic(TaskValidator.class)) {
            validator.when(() -> TaskValidator.validateTaskBeforeCreation(any(Task.class)))
                    .then(invocationOnMock -> null);
            when(mockedRepository.findByNameIgnoreCase(Mockito.anyString()))
                    .thenReturn(Optional.empty());

            Task task = new Task();
            when(mockedRepository.save(any(Task.class)))
                    .thenReturn(new Task());

            Task expected = new Task();
            Task actual = service.create(task);

            verify(mockedRepository, times(1)).save(any(Task.class));
            assertEquals(expected, actual);
            assertEquals(expected.getId(), actual.getId());
        }
    }

    @Test
    public void test_create_withValidNonUniqueSingleTask() {
        try (MockedStatic<TaskValidator> validator = Mockito.mockStatic(TaskValidator.class)) {
            validator.when(() -> TaskValidator.validateTaskBeforeCreation(any(Task.class)))
                    .then(invocationOnMock -> null);

            Task task = new Task();
            when(mockedRepository.findByNameIgnoreCase(any()))
                    .thenReturn(Optional.of(new Task()));

            Task expected = new Task();
            Task actual = service.create(task);

            verify(mockedRepository, never()).save(any(Task.class));
            assertEquals(expected, actual);
            assertEquals(expected.getId(), actual.getId());
        }
    }

    @Test
    public void test_create_withInvalidSingleTask() {
        try (MockedStatic<TaskValidator> validator = Mockito.mockStatic(TaskValidator.class)) {
            validator.when(() -> TaskValidator.validateTaskBeforeCreation(any(Task.class)))
                    .thenThrow(new TaskValidatorException("Task name cannot be null or empty."));
            Task task = new Task();
            assertThrows(TaskValidatorException.class, () -> service.create(task));
            verify(mockedRepository, never()).save(any(Task.class));
        }
    }

    @Test
    public void test_create_withValidUniqueMultipleTasks() {
        try (MockedStatic<TaskValidator> validator = Mockito.mockStatic(TaskValidator.class)) {
            validator.when(() -> TaskValidator.validateTaskBeforeCreation(any(Task.class)))
                    .then(invocationOnMock -> null);
            when(mockedRepository.findAll())
                    .thenReturn(new ArrayList<>());

            List<Task> tasks = createTasksWithoutId();
            when(mockedRepository.saveAll(anyList()))
                    .thenReturn(createTasksWithId());

            List<Task> expected = createTasksWithId();
            List<Task> actual = service.create(tasks);

            verify(mockedRepository, times(1)).saveAll(anyList());
            assertEquals(expected, actual);
            verifyTasksId(expected, actual);
        }
    }

    private List<Task> createTasksWithId() {
        return List.of(
                new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression"),
                new Task(2, PAUSED, "taskName2", null, "taskIdToExecute", "cronExpression"),
                new Task(3, ACTIVE, "taskName3", null, "taskIdToExecute", "cronExpression")
        );
    }

    private List<Task> createTasksWithoutId() {
        return List.of(
                new Task(-1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression"),
                new Task(-1, PAUSED, "taskName2", null, "taskIdToExecute", "cronExpression"),
                new Task(-1, ACTIVE, "taskName3", null, "taskIdToExecute", "cronExpression")
        );
    }

    private void verifyTasksId(final List<Task> expected, final List<Task> actual) {
        for (int i = 0; i < expected.size() && i < actual.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
        }
    }

    @Test
    public void test_create_withInvalidMultipleTasks() {
        try (MockedStatic<TaskValidator> validator = Mockito.mockStatic(TaskValidator.class)) {
            validator.when(() -> TaskValidator.validateTaskBeforeCreation(any(Task.class)))
                    .thenThrow(new TaskValidatorException("Task name cannot be null or empty."));
            List<Task> tasks = List.of(new Task(), new Task(), new Task());
            assertThrows(TaskValidatorException.class, () -> service.create(tasks));
            verify(mockedRepository, never()).saveAll(anyList());
        }
    }

    @Test
    public void test_findById_withValidIdAndExistentTask() {
        when(mockedRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Task()));
        Task expected = new Task();
        Task actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    public void test_findById_withValidIdAndNonExistentTask() {
        when(mockedRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> service.findById(-1));
    }

    @Test
    public void test_findByFields_withExistentFields_withoutPagination() {
        when(mockedRepository.findByFields(anyMap(), anyBoolean()))
                .thenReturn(List.of(new Task(), new Task()));
        List<Task> expected = List.of(new Task(), new Task());
        List<Task> actual = service.findByFields(Map.of("name", "taskName"), false);
        assertEquals(expected, actual);
    }

    @Test
    public void test_findByFields_withExistentFields_withPagination() {
        try (MockedStatic<PageRequestValidator> validator = Mockito.mockStatic(PageRequestValidator.class)) {
            validator.when(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class), any(Class.class)))
                    .then(invocationOnMock -> null);
            when(mockedRepository.findByFields(anyMap(), anyBoolean()))
                    .thenReturn(List.of(new Task(), new Task()));
            Page<Task> expected = new PageImpl<>(List.of(new Task(), new Task()), Pageable.ofSize(2), 2);
            Page<Task> actual = service.findByFields(Map.of("name", "taskName"), false, new PageRequestDTO(0, 2, null));
            assertEquals(expected, actual);
            validator.verify(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class), any(Class.class)));
        }
    }

    @Test
    public void test_findByFields_withNonExistentFields_withoutPagination() {
        when(mockedRepository.findByFields(anyMap(), anyBoolean()))
                .thenReturn(new ArrayList<>());
        List<Task> actual = service.findByFields(Map.of("nonExistentField", "fieldValue"), false);
        assertEquals(0, actual.size());
    }

    @Test
    public void test_findByFields_withNonExistentFields_withPagination() {
        try (MockedStatic<PageRequestValidator> validator = Mockito.mockStatic(PageRequestValidator.class)) {
            validator.when(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class), any(Class.class)))
                    .then(invocationOnMock -> null);
            when(mockedRepository.findByFields(anyMap(), anyBoolean()))
                    .thenReturn(new ArrayList<>());
            Page<Task> actual = service.findByFields(Map.of("nonExistentField", "fieldValue"), false,
                    new PageRequestDTO(0, 3, null));
            assertEquals(0, actual.getContent().size());
            validator.verify(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class), any(Class.class)));
        }
    }

    @Test
    public void test_findAll_withoutPagination() {
        when(mockedRepository.findAll())
                .thenReturn(createTasksWithId());
        List<Task> expected = createTasksWithId();
        List<Task> actual = service.findAll();
        assertEquals(expected, actual);
        verifyTasksId(expected, actual);
    }

    @Test
    public void test_findAll_withPagination() {
        try (MockedStatic<PageRequestValidator> validator = Mockito.mockStatic(PageRequestValidator.class)) {
            validator.when(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class), any(Class.class)))
                    .then(invocationOnMock -> null);
            when(mockedRepository.findAll(any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(createTasksWithId(), Pageable.ofSize(3), 3));
            Page<Task> expected = new PageImpl<>(createTasksWithId(), Pageable.ofSize(3), 3);
            Page<Task> actual = service.findAll(new PageRequestDTO(0, 3, null));
            assertEquals(expected, actual);
            verifyTasksId(expected.getContent(), actual.getContent());
            validator.verify(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class), any(Class.class)));
        }
    }

    @Test
    public void test_update_withValidExistentTask() {
        try (MockedStatic<TaskValidator> validator = Mockito.mockStatic(TaskValidator.class)) {
            validator.when(() -> TaskValidator.validateTaskBeforeUpdating(any(Task.class)))
                    .then(invocationOnMock -> null);
            when(mockedRepository.findById(anyLong()))
                    .thenReturn(Optional.of(new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute",
                            "cronExpression")));
            when(mockedRepository.findByNameIgnoreCase(anyString()))
                    .thenReturn(Optional.empty());

            Task updatedTask = new Task(1, PAUSED, "updatedTaskName", null, null, null);
            when(mockedRepository.save(updatedTask))
                    .thenReturn(new Task(1, updatedTask.getStatus(), updatedTask.getName(), null,
                            "taskIdToExecute", "cronExpression"));

            Task expected = new Task(1, updatedTask.getStatus(), updatedTask.getName(), null,
                    "taskIdToExecute", "cronExpression");
            Task actual = service.update(updatedTask);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void test_update_withValidNonExistentTask() {
        try (MockedStatic<TaskValidator> validator = Mockito.mockStatic(TaskValidator.class)) {
            validator.when(() -> TaskValidator.validateTaskBeforeUpdating(any(Task.class)))
                    .then(invocationOnMock -> null);
            when(mockedRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            Task updatedTask = new Task(-1, PAUSED, "updatedTaskName", null, null, null);

            assertThrows(TaskNotFoundException.class, () -> service.update(updatedTask));

            verify(mockedRepository, times(1)).findById(anyLong());
            verify(mockedRepository, never()).save(any());
            verify(mockedRepository, never()).findByNameIgnoreCase(anyString());
        }
    }

    @Test
    public void test_update_withValidTaskThatAlreadyExistsAfterUpdating() {
        try (MockedStatic<TaskValidator> validator = Mockito.mockStatic(TaskValidator.class)) {
            validator.when(() -> TaskValidator.validateTaskBeforeUpdating(any(Task.class)))
                    .then(invocationOnMock -> null);
            when(mockedRepository.findById(anyLong()))
                    .thenReturn(Optional.of(new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute",
                            "cronExpression")));
            when(mockedRepository.findByNameIgnoreCase(anyString()))
                    .thenReturn(Optional.of(new Task(2, ACTIVE, "updatedTaskName1", null, "taskIdToExecute",
                            "cronExpression")));

            Task updatedTask = new Task(1, ACTIVE, "updatedTaskName1", null, null, null);
            updatedTask.setId(1);

            assertThrows(TaskAlreadyExistsException.class, () -> service.update(updatedTask));

            verify(mockedRepository, times(1)).findById(anyLong());
            verify(mockedRepository, never()).save(any());
            verify(mockedRepository, times(1)).findByNameIgnoreCase(anyString());
        }
    }

    @Test
    public void test_delete_withValidExistentTaskId() {
        when(mockedRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Task()));
        service.delete(1);
        verify(mockedRepository, times(1)).findById(anyLong());
        verify(mockedRepository, times(1)).deleteById(anyLong());
    }
}
