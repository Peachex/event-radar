package by.klevitov.eventwebapp.healthcheck.service.impl;

import by.klevitov.eventwebapp.eventapp.client.EventPersistorClient;
import by.klevitov.eventwebapp.healthcheck.service.ResourceCheckService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static by.klevitov.eventwebapp.healthcheck.constant.HealthCheckExceptionMessage.EVENT_PERSISTOR_IS_NOT_AVAILABLE;
import static by.klevitov.eventwebapp.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_DOWN_VALUE;
import static by.klevitov.eventwebapp.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_KEY;
import static by.klevitov.eventwebapp.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_UP_VALUE;

@Log4j2
@Service
public class ResourceCheckServiceImpl implements ResourceCheckService {
    private final EventPersistorClient eventPersistorClient;

    @Autowired
    public ResourceCheckServiceImpl(EventPersistorClient eventPersistorClient) {
        this.eventPersistorClient = eventPersistorClient;
    }

    @Override
    public Pair<String, String> checkEventPersistorAvailabilityAndGetResult() {
        String eventPersistorStatus = isEventPersistorAvailable() ? EVENT_PERSISTOR_HEALTH_UP_VALUE
                : EVENT_PERSISTOR_HEALTH_DOWN_VALUE;
        return Pair.of(EVENT_PERSISTOR_HEALTH_KEY, eventPersistorStatus);
    }

    private boolean isEventPersistorAvailable() {
        boolean isAvailable = true;
        try {
            eventPersistorClient.healthCheck();
        } catch (Exception e) {
            isAvailable = false;
            log.error(String.format(EVENT_PERSISTOR_IS_NOT_AVAILABLE, e.getMessage()));
        }
        return isAvailable;
    }
}
