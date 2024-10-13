package by.klevitov.eventpersistor.healthcheck.service.impl;

import by.klevitov.eventpersistor.healthcheck.service.ResourceCheckService;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.net.ConnectException;
import java.util.ArrayList;

import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_DOWN_VALUE;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_UP_VALUE;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_DOWN_VALUE;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_KEY;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

public class ResourceCheckServiceImplTest {
    private static ResourceCheckService service;
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private static ListCollectionsIterable<Document> mongoCollections;
    private static ConnectionFactory connectionFactory;
    private static Connection brokerConnection;

    @BeforeAll
    public static void init() {
        mongoClient = Mockito.mock(MongoClient.class);
        mongoDatabase = Mockito.mock(MongoDatabase.class);
        mongoCollections = Mockito.mock(ListCollectionsIterable.class);
        connectionFactory = Mockito.mock(ConnectionFactory.class);
        brokerConnection = Mockito.mock(Connection.class);
        service = new ResourceCheckServiceImpl(mongoClient, connectionFactory);
    }

    @Test
    public void test_checkDatabaseAvailabilityAndGetResult_withAvailableDatabase() {
        when(mongoClient.getDatabase(any()))
                .thenReturn(mongoDatabase);
        when(mongoDatabase.listCollections())
                .thenReturn(mongoCollections);
        when(mongoCollections.into(anyList()))
                .thenReturn(new ArrayList<>());

        Pair<String, String> expected = Pair.of(DATABASE_HEALTH_KEY, DATABASE_HEALTH_UP_VALUE);
        Pair<String, String> actual = service.checkDatabaseAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkDatabaseAvailabilityAndGetResult_withNotAvailableDatabase() {
        when(mongoClient.getDatabase(any()))
                .thenThrow(new MongoTimeoutException("Timed out while waiting for a server."));

        Pair<String, String> expected = Pair.of(DATABASE_HEALTH_KEY, DATABASE_HEALTH_DOWN_VALUE);
        Pair<String, String> actual = service.checkDatabaseAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkMessageBrokerAvailabilityAndGetResult_withAvailableBroker() {
        when(connectionFactory.createConnection())
                .thenReturn(brokerConnection);
        Pair<String, String> expected = Pair.of(MESSAGE_BROKER_HEALTH_KEY, MESSAGE_BROKER_HEALTH_UP_VALUE);
        Pair<String, String> actual = service.checkMessageBrokerAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkMessageBrokerAvailabilityAndGetResult_withNotAvailableBroker() {
        when(connectionFactory.createConnection())
                .thenThrow(new AmqpConnectException(new ConnectException("java.net.ConnectException: "
                        + "Connection refused: getsockopt")));
        Pair<String, String> expected = Pair.of(MESSAGE_BROKER_HEALTH_KEY, MESSAGE_BROKER_HEALTH_DOWN_VALUE);
        Pair<String, String> actual = service.checkMessageBrokerAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }
}
