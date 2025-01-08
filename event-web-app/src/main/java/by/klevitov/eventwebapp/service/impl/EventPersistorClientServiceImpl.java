package by.klevitov.eventwebapp.service.impl;

import by.klevitov.eventradarcommon.client.EventPersistorClient;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventwebapp.service.EventPersistorClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.MapUtils.isNotEmpty;

@Service
public class EventPersistorClientServiceImpl implements EventPersistorClientService {
    private final EventPersistorClient client;

    @Autowired
    public EventPersistorClientServiceImpl(EventPersistorClient client) {
        this.client = client;
    }

    @Override
    public AbstractEventDTO createEvent(AbstractEventDTO event) {
        return client.create(event);
    }

    @Override
    public List<AbstractEventDTO> createEvents(List<AbstractEventDTO> eventsDTO) {
        return client.create(eventsDTO);
    }

    @Override
    public AbstractEventDTO findEventById(String id) {
        return client.findById(id);
    }

    @Override
    public List<AbstractEventDTO> findEventsByFields(Map<String, Object> fields) {
        return (isNotEmpty(fields) ? client.findByFields(fields) : new ArrayList<>());
    }

    @Override
    public List<AbstractEventDTO> findAllEvents() {
        return client.findAll();
    }

    @Override
    public AbstractEventDTO updateEvent(AbstractEventDTO updatedEvent) {
        return client.update(updatedEvent);
    }

    @Override
    public void deleteEvent(String id) {
        client.delete(id);
    }

    //todo Restrict access to 'create', 'update' and 'delete' functionality. Only user with certain role should have
    // permission to perform write operations on events.
}
