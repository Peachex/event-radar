package by.klevitov.eventmanager.service.impl;

import by.klevitov.eventmanager.exception.SyncTaskExecutorFactoryException;
import by.klevitov.eventmanager.exception.TaskManagerServiceException;
import by.klevitov.eventmanager.executor.SyncTaskExecutor;
import by.klevitov.eventmanager.factory.SyncTaskExecutorFactory;
import by.klevitov.eventmanager.service.TaskManagerService;
import by.klevitov.eventmanager.service.impl.TaskManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskManagerServiceImplTest {
    private TaskManagerService taskManagerService;
    private SyncTaskExecutorFactory mockedTaskRegistry;
    private SyncTaskExecutor mockedTaskExecutor;

    @BeforeEach
    public void setUp() {
        mockedTaskRegistry = Mockito.mock(SyncTaskExecutorFactory.class);
        mockedTaskExecutor = Mockito.mock(SyncTaskExecutor.class);
        taskManagerService = new TaskManagerServiceImpl(mockedTaskRegistry);
    }

    @Test
    public void test_executeTask_withNotNullExistentTaskId() throws InterruptedException {
        when(mockedTaskRegistry.getTaskExecutor(anyString()))
                .thenReturn(mockedTaskExecutor);
        taskManagerService.executeTask("taskId");
        verify(mockedTaskRegistry, times(1)).getTaskExecutor(anyString());

        // Sleep is needed for successful verification, since task is executed in a separate thread.
        Thread.sleep(500);
        verify(mockedTaskExecutor, times(1)).execute();
    }

    @Test
    public void test_executeTask_withNullTaskId() {
        assertThrowsExactly(TaskManagerServiceException.class, () -> taskManagerService.executeTask(null));
        verify(mockedTaskRegistry, never()).getTaskExecutor(anyString());
        verify(mockedTaskExecutor, never()).execute();
    }

    @Test
    public void test_executeTask_withNonExistentTaskId() {
        when(mockedTaskRegistry.getTaskExecutor(anyString()))
                .thenThrow(SyncTaskExecutorFactoryException.class);
        assertThrowsExactly(SyncTaskExecutorFactoryException.class,
                () -> taskManagerService.executeTask("nonExistentTaskId"));
        verify(mockedTaskRegistry, times(1)).getTaskExecutor(anyString());
        verify(mockedTaskExecutor, never()).execute();
    }

    @Test
    public void test_retrieveTaskExecutorIds() {
        final String taskName = "taskName";
        when(mockedTaskRegistry.getTaskExecutorsMapWithKey())
                .thenReturn(Map.of(taskName, mockedTaskExecutor));
        List<String> expected = List.of(taskName);
        List<String> actual = taskManagerService.retrieveTaskExecutorIds();
        assertEquals(expected, actual);
        verify(mockedTaskRegistry, times(1)).getTaskExecutorsMapWithKey();
    }
}
