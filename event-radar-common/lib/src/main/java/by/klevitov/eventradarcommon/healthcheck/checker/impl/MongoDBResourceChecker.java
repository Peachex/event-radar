package by.klevitov.eventradarcommon.healthcheck.checker.impl;

import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import com.mongodb.client.MongoClient;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckExceptionMessage.DATABASE_IS_NOT_AVAILABLE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;

@Log4j2
public class MongoDBResourceChecker implements ResourceChecker {
    private final MongoClient mongoClient;
    private final String databaseName;

    public MongoDBResourceChecker(MongoClient mongoClient, String databaseName) {
        this.mongoClient = mongoClient;
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
            mongoClient.getDatabase(databaseName).listCollections().into(new ArrayList<>());
        } catch (Exception e) {
            isAvailable = false;
            log.error(String.format(DATABASE_IS_NOT_AVAILABLE, databaseName));
        }
        return isAvailable;
    }
}
