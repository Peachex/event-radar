package by.klevitov.eventwebapp.config;

import by.klevitov.eventradarcommon.client.EventPersistorClient;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.EventPersistorClientResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import by.klevitov.eventradarcommon.healthcheck.service.impl.ResourceCheckServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HealthCheckConfig {
    @Bean
    public ResourceCheckService resourceCheckService(final EventPersistorClient eventPersistorClient) {
        return new ResourceCheckServiceImpl(List.of(new EventPersistorClientResourceChecker(eventPersistorClient)));
    }
}
