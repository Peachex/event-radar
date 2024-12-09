package by.klevitov.eventmanager.manager.consumer;

import by.klevitov.eventmanager.manager.service.TaskManagerService;
import by.klevitov.eventradarcommon.messaging.TaskSchedulerMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SyncTaskConsumer {
    private final TaskManagerService taskManagerService;

    public SyncTaskConsumer(TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }

    @RabbitListener(queues = "${queue.task-scheduler}")
    public void consume(final TaskSchedulerMessage message) {
        final String taskIdToExecute = message.getTaskIdToExecute();
        taskManagerService.executeTask(taskIdToExecute);
    }

    //todo Log consumed message.
}
