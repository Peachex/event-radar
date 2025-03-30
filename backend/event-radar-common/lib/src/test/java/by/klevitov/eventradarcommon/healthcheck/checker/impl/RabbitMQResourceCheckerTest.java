package by.klevitov.eventradarcommon.healthcheck.checker.impl;

import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.net.ConnectException;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RabbitMQResourceCheckerTest {
    private static ResourceChecker resourceChecker;
    private static ConnectionFactory mockedConnectionFactory;

    @BeforeEach
    public void setUp() {
        mockedConnectionFactory = Mockito.mock(ConnectionFactory.class);
        resourceChecker = new RabbitMQResourceChecker(mockedConnectionFactory);
    }

    @Test
    public void test_checkMessageBrokerAvailability_withAvailableBroker() {
        Connection mockedBrokerConnection = Mockito.mock(Connection.class);
        when(mockedConnectionFactory.createConnection())
                .thenReturn(mockedBrokerConnection);
        Pair<String, String> expected = Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_UP_VALUE);
        Pair<String, String> actual = resourceChecker.checkResourceAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkMessageBrokerAvailability_withNotAvailableBroker() {
        when(mockedConnectionFactory.createConnection())
                .thenThrow(new AmqpConnectException(new ConnectException("java.net.ConnectException: "
                        + "Connection refused: getsockopt")));
        Pair<String, String> expected = Pair.of(MESSAGE_BROKER_HEALTH_KEY, HEALTH_DOWN_VALUE);
        Pair<String, String> actual = resourceChecker.checkResourceAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }
}
