package by.klevitov.synctaskscheduler.taskscheduler.consumer;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TestConsumer {
    @RabbitListener(queues = "${queue.task-scheduler}")
    public void consume(final Task message) {
        System.out.println("From consumer: " + message);
    }

    //todo Delete this class.
}
