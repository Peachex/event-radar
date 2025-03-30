package by.klevitov.eventradarcommon.healthcheck.service.impl;

import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import by.klevitov.eventradarcommon.healthcheck.service.ResourceCheckService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ResourceCheckServiceImpl implements ResourceCheckService {
    private final List<ResourceChecker> resourceCheckers;

    public ResourceCheckServiceImpl(List<ResourceChecker> resourceCheckers) {
        this.resourceCheckers = (resourceCheckers != null ? resourceCheckers : new ArrayList<>());
    }

    @Override
    public List<Pair<String, String>> checkResources() {
        List<Pair<String, String>> results = new ArrayList<>();
        for (ResourceChecker checker : resourceCheckers) {
            results.add(checker.checkResourceAvailabilityAndGetResult());
        }
        return results;
    }
}
