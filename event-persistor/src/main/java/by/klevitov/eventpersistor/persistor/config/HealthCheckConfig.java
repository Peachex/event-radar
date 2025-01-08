package by.klevitov.eventpersistor.persistor.config;

import by.klevitov.eventradarcommon.healthcheck.checker.impl.MongoDBResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import by.klevitov.eventradarcommon.healthcheck.service.impl.ResourceCheckServiceImpl;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HealthCheckConfig {
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public ResourceCheckService resourceCheckService(final MongoClient mongoClient) {
        return new ResourceCheckServiceImpl(List.of(new MongoDBResourceChecker(mongoClient, databaseName)));
    }
}
