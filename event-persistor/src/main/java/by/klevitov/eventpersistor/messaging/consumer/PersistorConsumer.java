package by.klevitov.eventpersistor.messaging.consumer;

import by.klevitov.eventpersistor.messaging.service.MessageService;
import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import static by.klevitov.eventradarcommon.util.MessageConverter.convertRequestFromBytes;
import static by.klevitov.eventradarcommon.util.MessageConverter.convertResponseToBytes;

@Component
public class PersistorConsumer {
    private final MessageService messageService;

    @Autowired
    public PersistorConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

    @RabbitListener(queues = "${queue.singleQueue}")
    @SendTo
    public byte[] processRequest(final byte[] requestInBytes) throws Exception {
        MessageRequest request = convertRequestFromBytes(requestInBytes);
        MessageResponse response = messageService.processAndRetrieveResult(request);
        return convertResponseToBytes(response);
    }

    //todo Separate queue for write/read operations might need to be created.
}
