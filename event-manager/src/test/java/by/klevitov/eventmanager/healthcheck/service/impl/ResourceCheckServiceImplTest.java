package by.klevitov.eventmanager.healthcheck.service.impl;

import by.klevitov.eventmanager.healthcheck.service.ResourceCheckService;
import by.klevitov.eventmanager.manager.client.EventPersistorClient;
import by.klevitov.eventmanager.manager.exception.EventPersistorClientException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.net.ConnectException;

import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_DOWN_VALUE;
import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_KEY;
import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.EVENT_PERSISTOR_HEALTH_UP_VALUE;
import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_DOWN_VALUE;
import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_KEY;
import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class ResourceCheckServiceImplTest {
    private static ResourceCheckService service;
    private static ConnectionFactory mockedConnectionFactory;
    private static Connection mockedBrokerConnection;
    private static EventPersistorClient mockedEventPersistorClient;

    @BeforeEach
    public void setUp() {
        mockedConnectionFactory = Mockito.mock(ConnectionFactory.class);
        mockedBrokerConnection = Mockito.mock(Connection.class);
        mockedEventPersistorClient = Mockito.mock(EventPersistorClient.class);
        service = new ResourceCheckServiceImpl(mockedConnectionFactory, mockedEventPersistorClient);
    }

    @Test
    public void test_checkMessageBrokerAvailabilityAndGetResult_withAvailableBroker() {
        when(mockedConnectionFactory.createConnection())
                .thenReturn(mockedBrokerConnection);
        Pair<String, String> expected = Pair.of(MESSAGE_BROKER_HEALTH_KEY, MESSAGE_BROKER_HEALTH_UP_VALUE);
        Pair<String, String> actual = service.checkMessageBrokerAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkMessageBrokerAvailabilityAndGetResult_withNotAvailableBroker() {
        when(mockedConnectionFactory.createConnection())
                .thenThrow(new AmqpConnectException(new ConnectException("java.net.ConnectException: "
                        + "Connection refused: getsockopt")));
        Pair<String, String> expected = Pair.of(MESSAGE_BROKER_HEALTH_KEY, MESSAGE_BROKER_HEALTH_DOWN_VALUE);
        Pair<String, String> actual = service.checkMessageBrokerAvailabilityAndGetResult();
        assertEquals(expected, actual);
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
