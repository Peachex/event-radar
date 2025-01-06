package by.klevitov.eventwebapp.healthcheck.web.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static by.klevitov.eventwebapp.healthcheck.constant.HealthCheckMessage.APPLICATION_HEALTH_KEY;
import static by.klevitov.eventwebapp.healthcheck.constant.HealthCheckMessage.APPLICATION_HEALTH_UP_VALUE;

@RestController
public class HealthController {
    @Autowired
    public HealthController() {
    }

    @GetMapping("/healthcheck")
    public Pair<String, String> checkHealth() {
        return Pair.of(APPLICATION_HEALTH_KEY, APPLICATION_HEALTH_UP_VALUE);
    }
}
