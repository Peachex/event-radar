package by.klevitov.eventpersistor.service;

import by.klevitov.eventpersistor.common.dto.PageRequestDTO;
import by.klevitov.eventpersistor.entity.Location;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface LocationService {
    Location create(final Location location);

    List<Location> create(final List<Location> locations);

    Location findById(final String id);

    List<Location> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch);

    Page<Location> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch,
                                final PageRequestDTO pageRequestDTO);

    List<Location> findAll();

    Page<Location> findAll(final PageRequestDTO pageRequestDTO);

    Location update(final Location updatedLocation);

    void delete(final String id);
}
