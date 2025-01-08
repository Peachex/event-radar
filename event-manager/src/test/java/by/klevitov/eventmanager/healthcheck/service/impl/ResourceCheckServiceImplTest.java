package by.klevitov.eventmanager.healthcheck.service.impl;

import by.klevitov.eventradarcommon.client.EventPersistorClient;
import by.klevitov.eventradarcommon.client.exception.EventPersistorClientException;
import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.EventPersistorClientResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.RabbitMQResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import by.klevitov.eventradarcommon.healthcheck.service.impl.ResourceCheckServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.net.ConnectException;
import java.util.List;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class ResourceCheckServiceImplTest {
    //todo Move healthcheck tests to event-radar-common.

    private static ResourceCheckService service;
    private static ConnectionFactory mockedConnectionFactory;
    private static Connection mockedBrokerConnection;
    private static EventPersistorClient mockedEventPersistorClient;

    @BeforeEach
    public void setUp() {
        mockedConnectionFactory = Mockito.mock(ConnectionFactory.class);
        mockedBrokerConnection = Mockito.mock(Connection.class);
        mockedEventPersistorClient = Mockito.mock(EventPersistorClient.class);
        List<ResourceChecker> resourceCheckers = List.of(
                new RabbitMQResourceChecker(mockedConnectionFactory),
                new EventPersistorClientResourceChecker(mockedEventPersistorClient)
        );
        service = new ResourceCheckServiceImpl(resourceCheckers);
    }

    @Test
    public void test_checkMessageBrokerAvailability_withAvailableBroker() {
        when(mockedConnectionFactory.createConnection())
                .thenReturn(mockedBrokerConnection);
        Pair<String, String> expected = Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_UP_VALUE);
        Pair<String, String> actual = service.checkResources().stream()
                .filter(t -> t.getLeft().equals(MESSAGE_BROKER_HEALTH_KEY))
                .findFirst()
                .get();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkMessageBrokerAvailability_withNotAvailableBroker() {
        when(mockedConnectionFactory.createConnection())
                .thenThrow(new AmqpConnectException(new ConnectException("java.net.ConnectException: "
                        + "Connection refused: getsockopt")));
        Pair<String, String> expected = Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_DOWN_VALUE);
        Pair<String, String> actual = service.checkResources().stream()
                .filter(t -> t.getLeft().equals(MESSAGE_BROKER_HEALTH_KEY))
                .findFirst()
                .get();
        assertEquals(expected, actual);
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
    public void test_checkEventPersistorAvailability_withNotAvailablePersistor() {
        doThrow(EventPersistorClientException.class).when(mockedEventPersistorClient).healthCheck();
        Pair<String, String> expected = Pair.of(EVENT_PERSISTOR_HEALTH_KEY, HEALTH_DOWN_VALUE);
        Pair<String, String> actual = service.checkResources().stream()
                .filter(t -> t.getLeft().equals(EVENT_PERSISTOR_HEALTH_KEY))
                .findFirst()
                .get();
        assertEquals(expected, actual);
    }
}
