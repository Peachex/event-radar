package by.klevitov.eventradarcommon.healthcheck.checker.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.JDBCException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PostgreSQLResourceCheckerTest {
    private static PostgreSQLResourceChecker resourceChecker;
    private static EntityManager mockedEntityManager;

    @BeforeEach
    public void setUp() {
        mockedEntityManager = Mockito.mock(EntityManager.class);
        resourceChecker = new PostgreSQLResourceChecker(mockedEntityManager, "databaseName");
    }

    @Test
    public void test_checkDatabaseAvailability_withAvailableDatabase() {
        Query mockedQuery = Mockito.mock(Query.class);
        when(mockedEntityManager.createNativeQuery(anyString()))
                .thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult())
                .thenReturn(null);
        Pair<String, String> expected = Pair.of(DATABASE_HEALTH_KEY, HEALTH_UP_VALUE);
        Pair<String, String> actual = resourceChecker.checkResourceAvailabilityAndGetResult();
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
        Pair<String, String> actual = resourceChecker.checkResourceAvailabilityAndGetResult();
        assertEquals(expected, actual);
    }
}
