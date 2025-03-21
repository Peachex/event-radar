package by.klevitov.eventpersistor.service;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface EventService {
    AbstractEvent create(final AbstractEvent event);

    List<AbstractEvent> create(final List<AbstractEvent> events);

    AbstractEvent findById(final String id);

    List<AbstractEvent> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch);

    Page<AbstractEvent> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch,
                                     final PageRequestDTO pageRequestDTO);

    List<AbstractEvent> findAll();

    Page<AbstractEvent> findAll(final PageRequestDTO pageRequestDTO);

    AbstractEvent update(final AbstractEvent updatedEvent);

    void delete(final String id);

    void deleteAll();
}
