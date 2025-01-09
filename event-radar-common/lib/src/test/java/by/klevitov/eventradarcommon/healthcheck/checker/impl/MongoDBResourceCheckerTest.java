package by.klevitov.eventradarcommon.healthcheck.checker.impl;

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

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

public class MongoDBResourceCheckerTest {
    private static MongoDBResourceChecker resourceChecker;
    private static MongoClient mockedMongoClient;

    @BeforeAll
    public static void init() {
        mockedMongoClient = Mockito.mock(MongoClient.class);
        resourceChecker = new MongoDBResourceChecker(mockedMongoClient, "databaseName");
    }

    @Test
    public void test_checkResourceAvailabilityAndGetResult_withAvailableDatabase() {
        MongoDatabase mockedMongoDatabase = Mockito.mock(MongoDatabase.class);
        ListCollectionsIterable<Document> mockedMongoCollections = Mockito.mock(ListCollectionsIterable.class);

        when(mockedMongoClient.getDatabase(any()))
                .thenReturn(mockedMongoDatabase);
        when(mockedMongoDatabase.listCollections())
                .thenReturn(mockedMongoCollections);
        when(mockedMongoCollections.into(anyList()))
                .thenReturn(new ArrayList<>());

        Pair<String, String> expected = Pair.of(DATABASE_HEALTH_KEY, HEALTH_UP_VALUE);
        Pair<String, String> actual = resourceChecker.checkResourceAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkResourceAvailabilityAndGetResult_withNotAvailableDatabase() {
        when(mockedMongoClient.getDatabase(any()))
                .thenThrow(new MongoTimeoutException("Timed out while waiting for a server."));
        Pair<String, String> expected = Pair.of(DATABASE_HEALTH_KEY, HEALTH_DOWN_VALUE);
        Pair<String, String> actual = resourceChecker.checkResourceAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }
}
