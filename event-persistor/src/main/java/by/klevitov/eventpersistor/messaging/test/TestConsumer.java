package by.klevitov.eventpersistor.messaging.test;

import by.klevitov.eventpersistor.messaging.service.MessageService;
import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestConsumer {
    @Autowired
    private MessageService service;

    @Autowired
    private ObjectMapper mapper;

    @RabbitListener(queues = "requestQueue")
    @SendTo
    public MessageResponse handleRequest(byte[] requestInBytes) throws InterruptedException, IOException {
        MessageRequest request = mapper.readValue(requestInBytes, MessageRequest.class);
        return service.processAndRetrieveResult(request);
    }

    //todo delete this class.
}
