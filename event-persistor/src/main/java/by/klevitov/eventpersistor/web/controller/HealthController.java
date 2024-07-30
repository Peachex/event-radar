package by.klevitov.eventpersistor.web.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final String HEALTH_KEY = "status";
    private final String HEALTH_VALUE = "UP.";

    @GetMapping("/healthcheck")
    public Pair<String, String> checkHealth() {
        return Pair.of(HEALTH_KEY, HEALTH_VALUE);
    }

    @GetMapping("/resourcecheck")
    public Pair<String, String> checkResource() {
        //todo Add resource check - check if database/message queue is available.
        return null;
    }
}
