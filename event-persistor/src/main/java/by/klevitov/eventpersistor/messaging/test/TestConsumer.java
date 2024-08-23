package by.klevitov.eventpersistor.messaging.test;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class TestConsumer {
    @RabbitListener(queues = "requestQueue")
    @SendTo
    public String handleRequest(String request) throws InterruptedException {
        System.out.println("Received request: " + request);
        return "Response to: " + request;
    }
}
