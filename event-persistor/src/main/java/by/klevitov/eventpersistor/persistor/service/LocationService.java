package by.klevitov.eventpersistor.persistor.service;

import by.klevitov.eventpersistor.persistor.entity.Location;

import java.util.List;

public interface LocationService {
    Location create(final Location location);

    List<Location> createMultiple(final List<Location> locations);

    Location findById(final String id);

    List<Location> findAll();

    Location update(final Location updatedLocation);

    void delete(final String id);
}
