package by.klevitov.eventpersistor.messaging.test;

import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Queue requestQueue;

    @Autowired
    private ObjectMapper mapper;

    public MessageResponse sendAndReceiveMessage(MessageRequest request) throws JsonProcessingException {
        // Send the message and receive the reply
        byte[] requestInBytes = mapper.writeValueAsBytes(request);
        MessageResponse response = (MessageResponse) rabbitTemplate.convertSendAndReceive(requestQueue.getName(),
                requestInBytes);
        return response;
    }

    //todo delete this class.
}
