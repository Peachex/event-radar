package by.klevitov.eventmanager.service;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;

import java.util.List;

public interface EventPersistorClientService {
    List<AbstractEventDTO> findEvents();

    List<AbstractEventDTO> createEvents(final List<AbstractEventDTO> eventsDTO);

    void deleteEvent(final String id);

    void deleteEventsByIds(final List<String> ids);

    void deleteEvents();
}
