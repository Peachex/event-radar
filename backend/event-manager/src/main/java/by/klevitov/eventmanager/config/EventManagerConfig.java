package by.klevitov.eventmanager.config;

import by.klevitov.eventparser.configuration.EventParserConfiguration;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventparser.service.impl.EventParserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventManagerConfig {
    static {
        EventParserConfiguration.setRequestsDelayMillis(3000L);
    }

    @Bean
    public EventParserService eventParserService() {
        return new EventParserServiceImpl();
    }
}
