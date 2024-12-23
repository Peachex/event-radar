package by.klevitov.eventmanager.manager.service.impl;

import by.klevitov.eventmanager.manager.client.EventPersistorClient;
import by.klevitov.eventmanager.manager.service.EventPersistorClientService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventPersistorClientServiceImpl implements EventPersistorClientService {
    private final EventPersistorClient client;

    @Autowired
    public EventPersistorClientServiceImpl(EventPersistorClient client) {
        this.client = client;
    }

    @Override
    public List<AbstractEventDTO> findEvents() {
        return client.findAll();
    }

    @Override
    public List<AbstractEventDTO> createEvents(List<AbstractEventDTO> eventsDTO) {
        return client.create(eventsDTO);
    }

    @Override
    public void deleteEvent(String id) {
        client.delete(id);
    }

    //todo Add deleteAll method to event persistor and then replace multiple delete calls with single request.
    @Override
    public void deleteEvents() {
        List<AbstractEventDTO> existentEvents = findEvents();
        existentEvents.forEach(e -> deleteEvent(e.getId()));
    }
}
