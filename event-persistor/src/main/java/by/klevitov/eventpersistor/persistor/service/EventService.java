package by.klevitov.eventpersistor.persistor.service;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;

import java.util.List;
import java.util.Optional;

public interface EventService {
    AbstractEvent create(final AbstractEvent event);

    Optional<AbstractEvent> findById(final String id);

    List<AbstractEvent> findAll();

    AbstractEvent update(final AbstractEvent updatedEvent);

    void delete(final String id);
}
