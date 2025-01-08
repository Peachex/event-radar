package by.klevitov.eventmanager.healthcheck.web.controller;

import by.klevitov.eventmanager.healthcheck.service.ResourceCheckService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.APPLICATION_HEALTH_KEY;
import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.APPLICATION_HEALTH_UP_VALUE;

@RestController
public class HealthController {
    private final ResourceCheckService service;

    @Autowired
    public HealthController(ResourceCheckService service) {
        this.service = service;
    }

    @GetMapping("/healthcheck")
    public Pair<String, String> checkHealth() {
        return Pair.of(APPLICATION_HEALTH_KEY, APPLICATION_HEALTH_UP_VALUE);
    }

    @GetMapping("/resourcecheck")
    public List<Pair<String, String>> checkResource() {
        //todo Try Spring Boot actuator.
        Pair<String, String> messageBrokerAvailabilityResult = service.checkMessageBrokerAvailabilityAndGetResult();
        Pair<String, String> eventPersistorAvailabilityResult = service.checkEventPersistorAvailabilityAndGetResult();
        return List.of(messageBrokerAvailabilityResult, eventPersistorAvailabilityResult);
    }
}
