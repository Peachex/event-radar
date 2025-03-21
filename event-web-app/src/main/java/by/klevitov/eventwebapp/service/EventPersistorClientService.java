package by.klevitov.eventwebapp.service;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageResponseDTO;
import by.klevitov.eventradarcommon.pagination.dto.SearchByFieldsRequestDTO;

import java.util.List;
import java.util.Map;

public interface EventPersistorClientService {
    AbstractEventDTO createEvent(final AbstractEventDTO event);

    List<AbstractEventDTO> createEvents(final List<AbstractEventDTO> events);

    AbstractEventDTO findEventById(final String id);

    List<AbstractEventDTO> findEventsByFields(final Map<String, Object> fields, final boolean isCombinedMatch);

    PageResponseDTO<AbstractEventDTO> findEventsByFields(final SearchByFieldsRequestDTO requestDTO);

    List<AbstractEventDTO> findAllEvents();

    PageResponseDTO<AbstractEventDTO> findAllEvents(final PageRequestDTO pageRequestDTO);

    AbstractEventDTO updateEvent(final AbstractEventDTO updatedEvent);

    void deleteEvent(final String id);

    void deleteEvents();
}
