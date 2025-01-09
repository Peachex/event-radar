package by.klevitov.eventpersistor.repository;

import by.klevitov.eventpersistor.entity.Location;

import java.util.List;
import java.util.Map;

public interface LocationRepository {
    List<Location> findByFields(Map<String, Object> fields);
}
