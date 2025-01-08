package by.klevitov.eventwebapp.healthcheck.service.impl;

import by.klevitov.eventwebapp.eventapp.client.EventPersistorClient;
import by.klevitov.eventwebapp.eventapp.exception.EventPersistorClientException;
import by.klevitov.eventwebapp.healthcheck.service.ResourceCheckService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static by.klevitov.eventwebapp.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_DOWN_VALUE;
import static by.klevitov.eventwebapp.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_KEY;
import static by.klevitov.eventwebapp.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

public class ResourceCheckServiceImplTest {
    private static ResourceCheckService service;
    private static EventPersistorClient mockedEventPersistorClient;

    @BeforeEach
    public void setUp() {
        mockedEventPersistorClient = Mockito.mock(EventPersistorClient.class);
        service = new ResourceCheckServiceImpl(mockedEventPersistorClient);
    }

    @Test
    public void test_checkEventPersistorAvailabilityAndGetResult_withAvailablePersistor() {
        Pair<String, String> expected = Pair.of(EVENT_PERSISTOR_HEALTH_KEY, EVENT_PERSISTOR_HEALTH_UP_VALUE);
        Pair<String, String> actual = service.checkEventPersistorAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkEventPersistorAvailabilityAndGetResult_withNotAvailablePersistor() {
        doThrow(EventPersistorClientException.class).when(mockedEventPersistorClient).healthCheck();
        Pair<String, String> expected = Pair.of(EVENT_PERSISTOR_HEALTH_KEY, EVENT_PERSISTOR_HEALTH_DOWN_VALUE);
        Pair<String, String> actual = service.checkEventPersistorAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }
}
