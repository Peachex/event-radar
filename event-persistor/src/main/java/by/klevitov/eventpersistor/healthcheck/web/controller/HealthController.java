package by.klevitov.eventpersistor.healthcheck.web.controller;

import by.klevitov.eventpersistor.healthcheck.service.ResourceCheckService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.APPLICATION_HEALTH_KEY;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.APPLICATION_HEALTH_UP_VALUE;

@RestController
public class HealthController {
    private final ResourceCheckService resourceCheckService;

    @Autowired
    public HealthController(ResourceCheckService resourceCheckService) {
        this.resourceCheckService = resourceCheckService;
    }

    @GetMapping("/healthcheck")
    public Pair<String, String> checkHealth() {
        return Pair.of(APPLICATION_HEALTH_KEY, APPLICATION_HEALTH_UP_VALUE);
    }

    @GetMapping("/resourcecheck")
    public List<Pair<String, String>> checkResource() {
        //todo Add resource check - check if message queue is available.
        Pair<String, String> databaseAvailabilityResult = resourceCheckService.checkDatabaseAvailabilityAndGetResult();
        return List.of(databaseAvailabilityResult);
    }

    //todo Select bean injection strategy.
}
