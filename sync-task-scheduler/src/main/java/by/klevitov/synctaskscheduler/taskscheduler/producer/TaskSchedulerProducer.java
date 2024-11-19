package by.klevitov.synctaskscheduler.taskscheduler.producer;

import by.klevitov.eventradarcommon.messaging.TaskSchedulerMessage;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskSchedulerProducer {
    private final RabbitTemplate rabbitTemplate;
    private final Queue taskSchedulerQueue;

    @Autowired
    public TaskSchedulerProducer(RabbitTemplate rabbitTemplate, Queue taskSchedulerQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.taskSchedulerQueue = taskSchedulerQueue;
    }

    public void send(final TaskSchedulerMessage message) {
        rabbitTemplate.convertAndSend(taskSchedulerQueue.getName(), message);
    }

    //todo May be add logger and capture message sending.
}
