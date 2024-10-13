package by.klevitov.eventpersistor.persistor.repository;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;

import java.util.List;
import java.util.Map;

public interface EventRepository {
    List<AbstractEvent> findByFields(Map<String, Object> fields);
}
