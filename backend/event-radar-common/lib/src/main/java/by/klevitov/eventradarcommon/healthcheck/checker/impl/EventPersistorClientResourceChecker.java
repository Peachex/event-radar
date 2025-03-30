package by.klevitov.eventradarcommon.healthcheck.checker.impl;

import by.klevitov.eventradarcommon.client.EventPersistorClient;
import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckExceptionMessage.EVENT_PERSISTOR_IS_NOT_AVAILABLE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;

@Log4j2
public class EventPersistorClientResourceChecker implements ResourceChecker {
    private final EventPersistorClient eventPersistorClient;

    public EventPersistorClientResourceChecker(EventPersistorClient eventPersistorClient) {
        this.eventPersistorClient = eventPersistorClient;
    }

    @Override
    public Pair<String, String> checkResourceAvailabilityAndGetResult() {
        String eventPersistorStatus = isEventPersistorAvailable() ? HEALTH_UP_VALUE : HEALTH_DOWN_VALUE;
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
