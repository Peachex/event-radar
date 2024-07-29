package by.klevitov.eventpersistor.web.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class HealthController {
    private final String HEALTH_KEY = "status";
    private final String HEALTH_VALUE = "UP.";

    @GetMapping("/healthcheck")
    public Pair<String, String> checkHealth() {
        return Pair.of(HEALTH_KEY, HEALTH_VALUE);
    }

    //fixme Fix issue with logger - write logs to console and file + keep original spring console logs.

    @GetMapping("/resourcecheck")
    public Pair<String, String> checkResource() {
        //todo Add resource check - check if database/message queue is available.
        log.warn("Null response!");
        log.info("Info 123");
        return null;
    }
}
