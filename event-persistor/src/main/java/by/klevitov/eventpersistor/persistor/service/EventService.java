package by.klevitov.eventpersistor.persistor.service;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;

import java.util.List;

public interface EventService {
    AbstractEvent create(final AbstractEvent event);

    List<AbstractEvent> create(final List<AbstractEvent> events);

    AbstractEvent findById(final String id);

    List<AbstractEvent> findAll();

    AbstractEvent update(final AbstractEvent updatedEvent);

    void delete(final String id);
}
