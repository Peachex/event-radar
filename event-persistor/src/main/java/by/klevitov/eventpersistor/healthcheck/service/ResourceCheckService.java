package by.klevitov.eventpersistor.healthcheck.service;

import org.apache.commons.lang3.tuple.Pair;

public interface ResourceCheckService {
    Pair<String, String> checkDatabaseAvailabilityAndGetResult();
}
