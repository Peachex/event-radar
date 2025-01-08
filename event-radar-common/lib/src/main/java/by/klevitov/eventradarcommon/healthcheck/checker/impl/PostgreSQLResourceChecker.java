package by.klevitov.eventradarcommon.healthcheck.checker.impl;

import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckExceptionMessage.DATABASE_IS_NOT_AVAILABLE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;

@Log4j2
public class PostgreSQLResourceChecker implements ResourceChecker {
    private static final String DATABASE_HEALTH_CHECK_SQL_QUERY = "SELECT 1;";
    private final EntityManager entityManager;
    private final String databaseName;

    public PostgreSQLResourceChecker(EntityManager entityManager, String databaseName) {
        this.entityManager = entityManager;
        this.databaseName = databaseName;
    }

    @Override
    public Pair<String, String> checkResourceAvailabilityAndGetResult() {
        String databaseStatus = isDatabaseAvailable() ? HEALTH_UP_VALUE : HEALTH_DOWN_VALUE;
        return Pair.of(DATABASE_HEALTH_KEY, databaseStatus);
    }

    private boolean isDatabaseAvailable() {
        boolean isAvailable = true;
        try {
            entityManager.createNativeQuery(DATABASE_HEALTH_CHECK_SQL_QUERY).getSingleResult();
        } catch (Exception e) {
            isAvailable = false;
            log.error(String.format(DATABASE_IS_NOT_AVAILABLE, databaseName));
        }
        return isAvailable;
    }
}
