package by.klevitov.synctaskscheduler.healthcheck.service.impl;

import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.PostgreSQLResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.checker.impl.RabbitMQResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import by.klevitov.eventradarcommon.healthcheck.service.impl.ResourceCheckServiceImpl;
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
import java.util.List;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ResourceCheckServiceImplTest {
    //todo Move healthcheck tests to event-radar-common.

    private static ResourceCheckService service;
    private static EntityManager mockedEntityManager;
    private static ConnectionFactory mockedConnectionFactory;
    private static Connection mockedBrokerConnection;

    @BeforeEach
    public void setUp() {
        mockedEntityManager = Mockito.mock(EntityManager.class);
        mockedConnectionFactory = Mockito.mock(ConnectionFactory.class);
        mockedBrokerConnection = Mockito.mock(Connection.class);
        List<ResourceChecker> resourceCheckers = List.of(
                new PostgreSQLResourceChecker(mockedEntityManager, "database"),
                new RabbitMQResourceChecker(mockedConnectionFactory)
        );
        service = new ResourceCheckServiceImpl(resourceCheckers);
    }

    @Test
    public void test_checkDatabaseAvailability_withAvailableDatabase() {
        Query mockedQuery = Mockito.mock(Query.class);
        when(mockedEntityManager.createNativeQuery(anyString()))
                .thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult())
                .thenReturn(null);
        Pair<String, String> expected = Pair.of(DATABASE_HEALTH_KEY, HEALTH_UP_VALUE);
        Pair<String, String> actual = service.checkResources().stream()
                .filter(t -> t.getLeft().equals(DATABASE_HEALTH_KEY))
                .findFirst()
                .get();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkDatabaseAvailability_withNotAvailableDatabase() {
        Query mockedQuery = Mockito.mock(Query.class);
        when(mockedEntityManager.createNativeQuery(anyString()))
                .thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult())
                .thenThrow(new JDBCException("Connection is not available, request timed out after 3010ms.",
                        new SQLException()));
        Pair<String, String> expected = Pair.of(DATABASE_HEALTH_KEY, HEALTH_DOWN_VALUE);
        Pair<String, String> actual = service.checkResources().stream()
                .filter(t -> t.getLeft().equals(DATABASE_HEALTH_KEY))
                .findFirst()
                .get();
        assertEquals(expected, actual);
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
}
