package by.klevitov.eventpersistor.persistor.repository;

import by.klevitov.eventpersistor.persistor.entity.Location;

import java.util.List;
import java.util.Map;

public interface LocationRepository {
    List<Location> findByFields(Map<String, Object> fields);
}
