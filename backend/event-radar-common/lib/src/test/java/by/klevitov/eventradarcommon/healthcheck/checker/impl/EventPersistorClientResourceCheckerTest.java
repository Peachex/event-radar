package by.klevitov.eventradarcommon.healthcheck.checker.impl;

import by.klevitov.eventradarcommon.client.EventPersistorClient;
import by.klevitov.eventradarcommon.client.exception.EventPersistorClientException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

public class EventPersistorClientResourceCheckerTest {
    private static EventPersistorClientResourceChecker resourceChecker;
    private static EventPersistorClient mockedEventPersistorClient;

    @BeforeEach
    public void setUp(){
        mockedEventPersistorClient = Mockito.mock(EventPersistorClient.class);
        resourceChecker = new EventPersistorClientResourceChecker(mockedEventPersistorClient);
    }

    @Test
    public void test_checkResourceAvailabilityAndGetResult_withAvailablePersistor() {
        Pair<String, String> expected = Pair.of(EVENT_PERSISTOR_HEALTH_KEY, HEALTH_UP_VALUE);
        Pair<String, String> actual = resourceChecker.checkResourceAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkResourceAvailabilityAndGetResult_withNotAvailablePersistor() {
        doThrow(EventPersistorClientException.class).when(mockedEventPersistorClient).healthCheck();
        Pair<String, String> expected = Pair.of(EVENT_PERSISTOR_HEALTH_KEY, HEALTH_DOWN_VALUE);
        Pair<String, String> actual = resourceChecker.checkResourceAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }
}
