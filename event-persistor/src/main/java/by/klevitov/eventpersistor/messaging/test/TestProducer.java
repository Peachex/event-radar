package by.klevitov.eventpersistor.messaging.test;

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

    public String sendAndReceiveMessage(String request) {
        // Send the message and receive the reply
        String response = (String) rabbitTemplate.convertSendAndReceive(requestQueue.getName(), request);
        return response != null ? response : "No response received";
    }
}
