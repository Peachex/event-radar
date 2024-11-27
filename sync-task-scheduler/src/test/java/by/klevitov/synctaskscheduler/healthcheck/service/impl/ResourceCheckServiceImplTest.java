package by.klevitov.synctaskscheduler.healthcheck.service.impl;

import by.klevitov.synctaskscheduler.healthcheck.service.ResourceCheckService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.JDBCException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.net.ConnectException;
import java.sql.SQLException;

import static by.klevitov.synctaskscheduler.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_DOWN_VALUE;
import static by.klevitov.synctaskscheduler.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.synctaskscheduler.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_UP_VALUE;
import static by.klevitov.synctaskscheduler.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_DOWN_VALUE;
import static by.klevitov.synctaskscheduler.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_KEY;
import static by.klevitov.synctaskscheduler.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ResourceCheckServiceImplTest {
    private static ResourceCheckService service;
    private static EntityManager mockedEntityManager;
    private static ConnectionFactory mockedConnectionFactory;
    private static Connection mockedBrokerConnection;

    @BeforeEach
    public void setUp() {
        mockedEntityManager = Mockito.mock(EntityManager.class);
        mockedConnectionFactory = Mockito.mock(ConnectionFactory.class);
        mockedBrokerConnection = Mockito.mock(Connection.class);
        service = new ResourceCheckServiceImpl(mockedEntityManager, mockedConnectionFactory);
    }

    @Test
    public void test_checkDatabaseAvailabilityAndGetResult_withAvailableDatabase() {
        Query mockedQuery = Mockito.mock(Query.class);
        when(mockedEntityManager.createNativeQuery(anyString()))
                .thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult())
                .thenReturn(null);
        Pair<String, String> expected = Pair.of(DATABASE_HEALTH_KEY, DATABASE_HEALTH_UP_VALUE);
        Pair<String, String> actual = service.checkDatabaseAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkDatabaseAvailabilityAndGetResult_withNotAvailableDatabase() {
        Query mockedQuery = Mockito.mock(Query.class);
        when(mockedEntityManager.createNativeQuery(anyString()))
                .thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult())
                .thenThrow(new JDBCException("Connection is not available, request timed out after 3010ms.",
                        new SQLException()));
        Pair<String, String> expected = Pair.of(DATABASE_HEALTH_KEY, DATABASE_HEALTH_DOWN_VALUE);
        Pair<String, String> actual = service.checkDatabaseAvailabilityAndGetResult();
        assertEquals(expected, actual);
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
}
