package by.klevitov.eventmanager.manager.consumer;

import by.klevitov.eventmanager.manager.service.TaskManagerService;
import by.klevitov.eventradarcommon.messaging.TaskSchedulerMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SyncTaskConsumer {
    private static final String CONSUMER_RECEIVED_MESSAGE_LOG = "Task scheduler message has been received: '%s'.";
    private final TaskManagerService taskManagerService;

    public SyncTaskConsumer(TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }

    @RabbitListener(queues = "${queue.task-scheduler}")
    public void consume(final TaskSchedulerMessage message) {
        log.info(String.format(CONSUMER_RECEIVED_MESSAGE_LOG, message));
        final String taskIdToExecute = message.getTaskIdToExecute();
        taskManagerService.executeTask(taskIdToExecute);
    }
}
