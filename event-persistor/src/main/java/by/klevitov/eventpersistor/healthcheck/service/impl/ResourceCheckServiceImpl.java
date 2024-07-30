package by.klevitov.eventpersistor.healthcheck.service.impl;

import by.klevitov.eventpersistor.healthcheck.service.ResourceCheckService;
import com.mongodb.client.MongoClient;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static by.klevitov.eventpersistor.healthcheck.constant.ExceptionMessage.DATABASE_IS_NOT_AVAILABLE;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_DOWN_VALUE;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_KEY;
import static by.klevitov.eventpersistor.healthcheck.constant.HealthCheckMessage.DATABASE_HEALTH_UP_VALUE;

@Service
@Log4j2
public class ResourceCheckServiceImpl implements ResourceCheckService {
    private final MongoClient mongoClient;
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Autowired
    public ResourceCheckServiceImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Pair<String, String> checkDatabaseAvailabilityAndGetResult() {
        String databaseStatus = isDatabaseAvailable() ? DATABASE_HEALTH_UP_VALUE : DATABASE_HEALTH_DOWN_VALUE;
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
