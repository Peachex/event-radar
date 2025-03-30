package by.klevitov.eventradarcommon.healthcheck.web.controller;

import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.APPLICATION_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;

@RestController
public class CommonHealthController {
    private final ResourceCheckService service;

    @Autowired
    public CommonHealthController(ResourceCheckService service) {
        this.service = service;
    }

    @GetMapping("/healthcheck")
    public Pair<String, String> checkHealth() {
        return Pair.of(APPLICATION_HEALTH_KEY, HEALTH_UP_VALUE);
    }

    @GetMapping("/resourcecheck")
    public List<Pair<String, String>> checkResourceVersion2() {
        return service.checkResources();
    }
}
