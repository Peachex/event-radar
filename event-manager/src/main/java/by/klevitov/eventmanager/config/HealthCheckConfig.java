package by.klevitov.eventmanager.config;

import by.klevitov.eventradarcommon.client.EventPersistorClient;
import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.EventPersistorClientResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.RabbitMQResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import by.klevitov.eventradarcommon.healthcheck.service.impl.ResourceCheckServiceImpl;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HealthCheckConfig {
    @Bean
    public ResourceCheckService resourceCheckService(final ConnectionFactory connectionFactory,
                                                     final EventPersistorClient eventPersistorClient) {
        List<ResourceChecker> resourceCheckers = List.of(
                new RabbitMQResourceChecker(connectionFactory),
                new EventPersistorClientResourceChecker(eventPersistorClient)
        );
        return new ResourceCheckServiceImpl(resourceCheckers);
    }
}
