package by.klevitov.eventpersistor.service;

import by.klevitov.eventpersistor.entity.AbstractEvent;

import java.util.List;
import java.util.Map;

public interface EventService {
    AbstractEvent create(final AbstractEvent event);

    List<AbstractEvent> create(final List<AbstractEvent> events);

    AbstractEvent findById(final String id);

    List<AbstractEvent> findByFields(final Map<String, Object> fields);

    List<AbstractEvent> findAll();

    AbstractEvent update(final AbstractEvent updatedEvent);

    void delete(final String id);
}
