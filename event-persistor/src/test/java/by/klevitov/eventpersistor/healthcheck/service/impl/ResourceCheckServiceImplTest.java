package by.klevitov.eventpersistor.healthcheck.service.impl;

import by.klevitov.eventradarcommon.healthcheck.checker.impl.MongoDBResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import by.klevitov.eventradarcommon.healthcheck.service.impl.ResourceCheckServiceImpl;
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
import java.util.List;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

public class ResourceCheckServiceImplTest {
    //todo Move healthcheck tests to event-radar-common.

    private static ResourceCheckService service;
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private static ListCollectionsIterable<Document> mongoCollections;

    @BeforeAll
    public static void init() {
        mongoClient = Mockito.mock(MongoClient.class);
        mongoDatabase = Mockito.mock(MongoDatabase.class);
        mongoCollections = Mockito.mock(ListCollectionsIterable.class);
        service = new ResourceCheckServiceImpl(List.of(new MongoDBResourceChecker(mongoClient, "database")));
    }

    @Test
    public void test_checkDatabaseAvailability_withAvailableDatabase() {
        when(mongoClient.getDatabase(any()))
                .thenReturn(mongoDatabase);
        when(mongoDatabase.listCollections())
                .thenReturn(mongoCollections);
        when(mongoCollections.into(anyList()))
                .thenReturn(new ArrayList<>());

        List<Pair<String, String>> expected = List.of(Pair.of(DATABASE_HEALTH_KEY, HEALTH_UP_VALUE));
        List<Pair<String, String>> actual = service.checkResources();
        assertEquals(expected, actual);
    }

    @Test
    public void test_checkDatabaseAvailability_withNotAvailableDatabase() {
        when(mongoClient.getDatabase(any()))
                .thenThrow(new MongoTimeoutException("Timed out while waiting for a server."));

        List<Pair<String, String>> expected = List.of(Pair.of(DATABASE_HEALTH_KEY, HEALTH_DOWN_VALUE));
        List<Pair<String, String>> actual = service.checkResources();
        assertEquals(expected, actual);
    }
}
