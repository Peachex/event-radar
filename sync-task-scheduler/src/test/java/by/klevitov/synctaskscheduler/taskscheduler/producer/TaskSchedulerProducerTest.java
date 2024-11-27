package by.klevitov.synctaskscheduler.taskscheduler.producer;

import by.klevitov.eventradarcommon.messaging.TaskSchedulerMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskSchedulerProducerTest {
    private static TaskSchedulerProducer producer;
    private static RabbitTemplate mockedRabbitTemplate;
    private static Queue mockedTaskSchedulerQueue;

    @BeforeEach
    public void setUp() {
        mockedRabbitTemplate = Mockito.mock(RabbitTemplate.class);
        mockedTaskSchedulerQueue = Mockito.mock(Queue.class);
        producer = new TaskSchedulerProducer(mockedRabbitTemplate, mockedTaskSchedulerQueue);
    }

    @Test
    public void test_send() {
        doNothing().when(mockedRabbitTemplate).convertAndSend(anyString(), any(TaskSchedulerMessage.class));
        when(mockedTaskSchedulerQueue.getName())
                .thenReturn("taskSchedulerQueueName");
        producer.send(new TaskSchedulerMessage());
        verify(mockedTaskSchedulerQueue, times(1)).getName();
        verify(mockedRabbitTemplate, times(1)).convertAndSend(anyString(),
                any(TaskSchedulerMessage.class));
    }
}
