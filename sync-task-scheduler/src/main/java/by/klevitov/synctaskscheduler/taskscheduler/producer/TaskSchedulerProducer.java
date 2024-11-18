package by.klevitov.synctaskscheduler.taskscheduler.producer;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskSchedulerProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Queue taskSchedulerQueue;

    public void send(final Task message) {
        rabbitTemplate.convertAndSend(taskSchedulerQueue.getName(), message);
    }

    //todo May be add logger and capture message sending.
}
