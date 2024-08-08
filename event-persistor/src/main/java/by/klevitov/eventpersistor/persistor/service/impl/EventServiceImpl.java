package by.klevitov.eventpersistor.persistor.service.impl;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;

import java.util.List;
import java.util.Optional;

public class EventServiceImpl implements EventService {
    @Override
    public AbstractEvent create(AbstractEvent event) {
        return null;
    }

    @Override
    public Optional<AbstractEvent> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<AbstractEvent> findAll() {
        return List.of();
    }

    @Override
    public AbstractEvent update(AbstractEvent updatedEvent) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
