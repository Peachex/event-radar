package by.klevitov.eventradarcommon.healthcheck.service;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface ResourceCheckService {
    List<Pair<String, String>> checkResources();
}
