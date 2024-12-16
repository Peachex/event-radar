package by.klevitov.eventmanager.manager.config;

import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventparser.service.impl.EventParserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventManagerConfig {
    @Bean
    public EventParserService eventParserService() {
        return new EventParserServiceImpl();
    }
}
