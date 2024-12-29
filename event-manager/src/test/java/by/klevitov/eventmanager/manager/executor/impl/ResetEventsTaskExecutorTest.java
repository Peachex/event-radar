package by.klevitov.eventmanager.manager.executor.impl;

import by.klevitov.eventmanager.manager.service.EventFetcherService;
import by.klevitov.eventmanager.manager.service.EventPersistorClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ResetEventsTaskExecutorTest {
    private ResetEventsTaskExecutor resetEventsTaskExecutor;
    private EventFetcherService mockedFetcherService;
    private EventPersistorClientService mockedClientService;

    @BeforeEach
    public void setUp() {
        mockedFetcherService = Mockito.mock(EventFetcherService.class);
        mockedClientService = Mockito.mock(EventPersistorClientService.class);
        resetEventsTaskExecutor = new ResetEventsTaskExecutor(mockedFetcherService, mockedClientService);
    }

    @Test
    public void test_execute() throws Exception {
        resetEventsTaskExecutor.execute();
        verify(mockedClientService, times(1)).deleteEvents();
        verify(mockedFetcherService, times(1)).fetch();
        verify(mockedClientService, times(1)).createEvents(anyList());
    }

    @Test
    public void test_retrieveExecutorId() throws Exception {
        Field taskNameField = ResetEventsTaskExecutor.class.getDeclaredField("TASK_NAME");
        taskNameField.setAccessible(true);
        String expected = (String) taskNameField.get(taskNameField);
        String actual = resetEventsTaskExecutor.retrieveExecutorId();
        assertEquals(expected, actual);
    }
}
