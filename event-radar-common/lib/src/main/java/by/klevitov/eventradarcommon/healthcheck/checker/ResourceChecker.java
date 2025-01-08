package by.klevitov.eventradarcommon.healthcheck.checker;

import org.apache.commons.lang3.tuple.Pair;

public interface ResourceChecker {
    Pair<String, String> checkResourceAvailabilityAndGetResult();
}
