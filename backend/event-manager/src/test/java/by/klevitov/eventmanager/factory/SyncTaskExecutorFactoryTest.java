package by.klevitov.eventmanager.factory;

import by.klevitov.eventmanager.exception.SyncTaskExecutorFactoryException;
import by.klevitov.eventmanager.executor.SyncTaskExecutor;
import by.klevitov.eventmanager.factory.SyncTaskExecutorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class SyncTaskExecutorFactoryTest {
    private static final String TEST_TASK_NAME = "taskName";
    private SyncTaskExecutorFactory factory;
    private SyncTaskExecutor mockedTaskExecutor;

    @BeforeEach
    public void setUp() {
        mockedTaskExecutor = Mockito.mock(SyncTaskExecutor.class);
        when(mockedTaskExecutor.retrieveExecutorId())
                .thenReturn(TEST_TASK_NAME);
        factory = new SyncTaskExecutorFactory(List.of(mockedTaskExecutor));
    }

    @Test
    public void test_getTaskExecutor_withExistentTask() {
        SyncTaskExecutor expected = mockedTaskExecutor;
        SyncTaskExecutor actual = factory.getTaskExecutor(TEST_TASK_NAME);
        assertEquals(expected, actual);
    }

    @Test
    public void test_getTaskExecutor_withNonExistentTask() {
        assertThrows(SyncTaskExecutorFactoryException.class, () -> factory.getTaskExecutor("-1"));
    }

    @Test
    public void test_getTaskExecutorsMapWithKey() {
        Map<String, SyncTaskExecutor> expected = Map.of(TEST_TASK_NAME, mockedTaskExecutor);
        Map<String, SyncTaskExecutor> actual = factory.getTaskExecutorsMapWithKey();
        assertEquals(expected, actual);
    }
}
