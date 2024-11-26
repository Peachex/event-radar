package by.klevitov.synctaskscheduler.healthcheck.service;

import org.apache.commons.lang3.tuple.Pair;

public interface ResourceCheckService {
    Pair<String, String> checkDatabaseAvailabilityAndGetResult();

    Pair<String, String> checkMessageBrokerAvailabilityAndGetResult();
}
