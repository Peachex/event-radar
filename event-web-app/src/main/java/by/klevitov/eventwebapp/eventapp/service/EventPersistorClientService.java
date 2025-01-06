package by.klevitov.eventwebapp.eventapp.service;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;

import java.util.List;
import java.util.Map;

public interface EventPersistorClientService {
    AbstractEventDTO createEvent(final AbstractEventDTO event);

    List<AbstractEventDTO> createEvents(final List<AbstractEventDTO> events);

    AbstractEventDTO findEventById(final String id);

    List<AbstractEventDTO> findEventsByFields(final Map<String, Object> fields);

    List<AbstractEventDTO> findAllEvents();

    AbstractEventDTO updateEvent(final AbstractEventDTO updatedEvent);

    void deleteEvent(final String id);
}
