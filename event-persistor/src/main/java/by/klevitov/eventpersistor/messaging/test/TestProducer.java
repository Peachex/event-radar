package by.klevitov.eventpersistor.messaging.test;

import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static by.klevitov.eventradarcommon.util.MessageConverter.convertRequestToBytes;
import static by.klevitov.eventradarcommon.util.MessageConverter.convertResponseFromBytes;

@Component
public class TestProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Queue requestQueue;

    public MessageResponse sendAndReceiveMessage(MessageRequest request) throws Exception {
        byte[] requestInBytes = convertRequestToBytes(request);
        byte[] responseInBytes = (byte[]) rabbitTemplate.convertSendAndReceive(requestQueue.getName(), requestInBytes);
        return convertResponseFromBytes(responseInBytes);
    }

    //todo delete this class.
}
