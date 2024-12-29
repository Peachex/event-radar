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

public class FetchNewEventsTaskExecutorTest {
    private FetchNewEventsTaskExecutor fetchNewEventsTaskExecutor;
    private EventFetcherService mockedFetcherService;
    private EventPersistorClientService mockedClientService;

    @BeforeEach
    public void setUp() {
        mockedFetcherService = Mockito.mock(EventFetcherService.class);
        mockedClientService = Mockito.mock(EventPersistorClientService.class);
        fetchNewEventsTaskExecutor = new FetchNewEventsTaskExecutor(mockedFetcherService, mockedClientService);
    }

    @Test
    public void test_execute() throws Exception {
        fetchNewEventsTaskExecutor.execute();
        verify(mockedFetcherService, times(1)).fetch();
        verify(mockedClientService, times(1)).createEvents(anyList());
    }

    @Test
    public void test_retrieveExecutorId() throws Exception {
        Field taskNameField = FetchNewEventsTaskExecutor.class.getDeclaredField("TASK_NAME");
        taskNameField.setAccessible(true);
        String expected = (String) taskNameField.get(taskNameField);
        String actual = fetchNewEventsTaskExecutor.retrieveExecutorId();
        assertEquals(expected, actual);
    }
}
