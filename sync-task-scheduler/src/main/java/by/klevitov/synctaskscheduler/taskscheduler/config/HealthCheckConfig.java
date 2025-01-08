package by.klevitov.synctaskscheduler.taskscheduler.config;

import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.PostgreSQLResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.RabbitMQResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import by.klevitov.eventradarcommon.healthcheck.service.impl.ResourceCheckServiceImpl;
import jakarta.persistence.EntityManager;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HealthCheckConfig {
    @Value("${spring.datasource.database}")
    private String databaseName;

    @Bean
    public ResourceCheckService resourceCheckService(final EntityManager entityManager,
                                                     final ConnectionFactory connectionFactory) {
        List<ResourceChecker> resourceCheckers = List.of(
                new PostgreSQLResourceChecker(entityManager, databaseName),
                new RabbitMQResourceChecker(connectionFactory)
        );
        return new ResourceCheckServiceImpl(resourceCheckers);
    }
}
