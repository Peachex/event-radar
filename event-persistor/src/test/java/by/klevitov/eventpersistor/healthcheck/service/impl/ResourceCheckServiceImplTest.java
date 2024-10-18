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

import java.util.ArrayList;

import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_DOWN_VALUE;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

public class ResourceCheckServiceImplTest {
    private static ResourceCheckService service;
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private static ListCollectionsIterable<Document> mongoCollections;

    @BeforeAll
    public static void init() {
        mongoClient = Mockito.mock(MongoClient.class);
        mongoDatabase = Mockito.mock(MongoDatabase.class);
        mongoCollections = Mockito.mock(ListCollectionsIterable.class);
        service = new ResourceCheckServiceImpl(mongoClient);
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
}
