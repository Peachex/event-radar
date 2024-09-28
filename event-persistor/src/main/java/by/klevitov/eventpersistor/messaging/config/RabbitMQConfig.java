package by.klevitov.eventpersistor.messaging.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${queue.single-queue}")
    private String requestQueueName;

    @Value("${spring.rabbitmq.reply-timeout}")
    private long replyTimeout;

    @Bean
    public Queue requestQueue() {
        return new Queue(requestQueueName, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyTimeout(replyTimeout); // Set a timeout for the reply
        return template;
    }

    //todo Delete queue data since it is needed only for applications that are going to use producer.
    //todo Catch and log exception when connecting to RabbitMQ.
}
