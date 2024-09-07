package by.klevitov.eventpersistor.messaging.consumer;

import by.klevitov.eventpersistor.messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersistorConsumer {
    private final MessageService messageService;

    @Autowired
    public PersistorConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

//    @RabbitListener(queues = "")
//    @SendTo
//    public MessageResponse processRequest(final MessageRequest request) {
//        return messageService.processAndRetrieveResult(request);
//    }

    //todo Add queue name. Separate queue for write/read operations might need to be created.
}
