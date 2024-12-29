package by.klevitov.eventmanager.manager.consumer;

import by.klevitov.eventmanager.manager.service.TaskManagerService;
import by.klevitov.eventradarcommon.messaging.TaskSchedulerMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SyncTaskConsumerTest {
    private SyncTaskConsumer consumer;
    private TaskManagerService mockedTaskManagerService;

    @BeforeEach
    public void setUp() {
        mockedTaskManagerService = Mockito.mock(TaskManagerService.class);
        consumer = new SyncTaskConsumer(mockedTaskManagerService);
    }

    @Test
    public void test_consume() {
        TaskSchedulerMessage taskMessage = new TaskSchedulerMessage("taskIdToExecute");
        doNothing().when(mockedTaskManagerService).executeTask(taskMessage.getTaskIdToExecute());
        consumer.consume(taskMessage);
        verify(mockedTaskManagerService, times(1)).executeTask(taskMessage.getTaskIdToExecute());
    }
}
