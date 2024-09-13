package by.klevitov.eventpersistor.messaging.test;

import by.klevitov.eventpersistor.messaging.service.MessageService;
import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    public byte[] handleRequest(byte[] requestInBytes) throws InterruptedException, IOException {
        mapper.registerModule(new JavaTimeModule());
        MessageRequest request = mapper.readValue(requestInBytes, MessageRequest.class);
        byte[] responseInBytes = mapper.writeValueAsBytes(service.processAndRetrieveResult(request));
        return responseInBytes;
    }

    //todo delete this class.
}
