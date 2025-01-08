package by.klevitov.eventwebapp.healthcheck.service.impl;

import by.klevitov.eventradarcommon.client.EventPersistorClient;
import by.klevitov.eventradarcommon.client.exception.EventPersistorClientException;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.EventPersistorClientResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import by.klevitov.eventradarcommon.healthcheck.service.impl.ResourceCheckServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

public class ResourceCheckServiceImplTest {
    private static ResourceCheckService service;
    private static EventPersistorClient mockedEventPersistorClient;

    @BeforeEach
    public void setUp() {
        mockedEventPersistorClient = Mockito.mock(EventPersistorClient.class);
        service = new ResourceCheckServiceImpl(List.of(
                new EventPersistorClientResourceChecker(mockedEventPersistorClient)));
    }

    @Test
    public void test_checkEventPersistorAvailability_withAvailablePersistor() {
        Pair<String, String> expected = Pair.of(EVENT_PERSISTOR_HEALTH_KEY, HEALTH_UP_VALUE);
        Pair<String, String> actual = service.checkResources().stream()
                .filter(t -> t.getLeft().equals(EVENT_PERSISTOR_HEALTH_KEY))
                .findFirst()
                .get();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkEventPersistorAvailabilityAndGetResult_withNotAvailablePersistor() {
        doThrow(EventPersistorClientException.class).when(mockedEventPersistorClient).healthCheck();
        Pair<String, String> expected = Pair.of(EVENT_PERSISTOR_HEALTH_KEY, HEALTH_DOWN_VALUE);
        Pair<String, String> actual = service.checkResources().stream()
                .filter(t -> t.getLeft().equals(EVENT_PERSISTOR_HEALTH_KEY))
                .findFirst()
                .get();
        assertEquals(expected, actual);
    }
}
