package by.klevitov.eventpersistor.messaging.test;

import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Queue requestQueue;

    @Autowired
    private ObjectMapper mapper;

    public MessageResponse sendAndReceiveMessage(MessageRequest request) throws IOException {
        // Send the message and receive the reply
        mapper.registerModule(new JavaTimeModule());
        byte[] requestInBytes = mapper.writeValueAsBytes(request);
        byte[] responseInBytes = (byte[]) rabbitTemplate.convertSendAndReceive(requestQueue.getName(), requestInBytes);
        MessageResponse response = mapper.readValue(responseInBytes, MessageResponse.class);
        return response;
    }

    //todo delete this class.
}
