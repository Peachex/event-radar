package by.klevitov.eventradarcommon.healthcheck.service.impl;

import by.klevitov.eventradarcommon.healthcheck.checker.impl.PostgreSQLResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.RabbitMQResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ResourceCheckServiceImplTest {
    private ResourceCheckService service;
    private PostgreSQLResourceChecker mockedPostgreSQLResourceChecker;
    private RabbitMQResourceChecker mockedRabbitMQResourceChecker;

    @BeforeEach
    public void setUp() {
        mockedPostgreSQLResourceChecker = Mockito.mock(PostgreSQLResourceChecker.class);
        mockedRabbitMQResourceChecker = Mockito.mock(RabbitMQResourceChecker.class);
        service = new ResourceCheckServiceImpl(List.of(mockedPostgreSQLResourceChecker, mockedRabbitMQResourceChecker));
    }

    @Test
    public void test_checkResources_withAvailableResources() {
        when(mockedPostgreSQLResourceChecker.checkResourceAvailabilityAndGetResult())
                .thenReturn(Pair.of(DATABASE_HEALTH_KEY, HEALTH_UP_VALUE));
        when(mockedRabbitMQResourceChecker.checkResourceAvailabilityAndGetResult())
                .thenReturn(Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_UP_VALUE));
        List<Pair<String, String>> expected = List.of(
                Pair.of(DATABASE_HEALTH_KEY, HEALTH_UP_VALUE),
                Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_UP_VALUE)
        );
        List<Pair<String, String>> actual = service.checkResources();
        assertEquals(expected, actual);
        verify(mockedPostgreSQLResourceChecker, times(1)).checkResourceAvailabilityAndGetResult();
        verify(mockedRabbitMQResourceChecker, times(1)).checkResourceAvailabilityAndGetResult();
    }

    @Test
    public void test_checkResources_withPartiallyAvailableResources() {
        when(mockedPostgreSQLResourceChecker.checkResourceAvailabilityAndGetResult())
                .thenReturn(Pair.of(DATABASE_HEALTH_KEY, HEALTH_UP_VALUE));
        when(mockedRabbitMQResourceChecker.checkResourceAvailabilityAndGetResult())
                .thenReturn(Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_DOWN_VALUE));
        List<Pair<String, String>> expected = List.of(
                Pair.of(DATABASE_HEALTH_KEY, HEALTH_UP_VALUE),
                Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_DOWN_VALUE)
        );
        List<Pair<String, String>> actual = service.checkResources();
        assertEquals(expected, actual);
        verify(mockedPostgreSQLResourceChecker, times(1)).checkResourceAvailabilityAndGetResult();
        verify(mockedRabbitMQResourceChecker, times(1)).checkResourceAvailabilityAndGetResult();
    }

    @Test
    public void test_checkResources_withNotAvailableResources() {
        when(mockedPostgreSQLResourceChecker.checkResourceAvailabilityAndGetResult())
                .thenReturn(Pair.of(DATABASE_HEALTH_KEY, HEALTH_DOWN_VALUE));
        when(mockedRabbitMQResourceChecker.checkResourceAvailabilityAndGetResult())
                .thenReturn(Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_DOWN_VALUE));
        List<Pair<String, String>> expected = List.of(
                Pair.of(DATABASE_HEALTH_KEY, HEALTH_DOWN_VALUE),
                Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_DOWN_VALUE)
        );
        List<Pair<String, String>> actual = service.checkResources();
        assertEquals(expected, actual);
        verify(mockedPostgreSQLResourceChecker, times(1)).checkResourceAvailabilityAndGetResult();
        verify(mockedRabbitMQResourceChecker, times(1)).checkResourceAvailabilityAndGetResult();
    }

    @Test
    public void test_checkResources_withNullResourceChecker() {
        service = new ResourceCheckServiceImpl(null);
        List<Pair<String, String>> expected = new ArrayList<>();
        List<Pair<String, String>> actual = service.checkResources();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkResources_withEmptyResourceChecker() {
        service = new ResourceCheckServiceImpl(new ArrayList<>());
        List<Pair<String, String>> expected = new ArrayList<>();
        List<Pair<String, String>> actual = service.checkResources();
        assertEquals(expected, actual);
    }
}
