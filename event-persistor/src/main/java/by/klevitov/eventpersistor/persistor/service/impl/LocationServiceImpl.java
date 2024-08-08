package by.klevitov.eventpersistor.persistor.service.impl;

import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.exception.LocationServiceException;
import by.klevitov.eventpersistor.persistor.repository.LocationRepository;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.LOCATION_NOT_FOUND;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_LOCATION;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_CITY;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_COUNTRY;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_ID;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Log4j2
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;

    @Autowired
    public LocationServiceImpl(LocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Location create(final Location location) {
        boolean validateWithId = false;
        validateLocation(location, validateWithId);
        return repository.findByCountryAndCityIgnoreCase(location.getCountry(), location.getCity())
                .orElseGet(() -> repository.insert(location));
    }

    private void validateLocation(final Location location, final boolean validateWithId) {
        if (location == null) {
            log.error(NULL_LOCATION);
            throw new LocationServiceException(NULL_LOCATION);
        }
        if (isEmpty(location.getCountry())) {
            log.error(NULL_OR_EMPTY_LOCATION_COUNTRY);
            throw new LocationServiceException(NULL_OR_EMPTY_LOCATION_COUNTRY);
        }
        if (isEmpty(location.getCity())) {
            log.error(NULL_OR_EMPTY_LOCATION_CITY);
            throw new LocationServiceException(NULL_OR_EMPTY_LOCATION_CITY);
        }
        if (validateWithId) {
            throwExceptionInCaseOfEmptyId(location.getId());
        }
    }

    @Override
    public List<Location> createMultiple(final List<Location> locations) {
        boolean validateWithId = false;
        locations.forEach(l -> validateLocation(l, validateWithId));
        List<Location> existentLocations = repository.findAll();
        List<Location> nonExistentLocations = createNonExistentLocationsList(locations, existentLocations);
        existentLocations.addAll(repository.saveAll(nonExistentLocations));
        Map<String, Location> existentLocationsWithKey = createLocationsMapWithCountryCityKey(existentLocations);
        updateLocationsWithId(existentLocations, existentLocationsWithKey);
        return existentLocations;
    }

    private List<Location> createNonExistentLocationsList(final List<Location> locations,
                                                          final List<Location> existentLocations) {
        List<Location> nonExistentLocations = new ArrayList<>();
        Map<String, Location> locationsWithKey = createLocationsMapWithCountryCityKey(existentLocations);
        locations.forEach(l -> {
            String locationKey = l.createIdBasedOnCountryAndCity();
            if (!locationsWithKey.containsKey(locationKey)) {
                nonExistentLocations.add(l);
            }
        });
        return nonExistentLocations;
    }

    private Map<String, Location> createLocationsMapWithCountryCityKey(final List<Location> locaitons) {
        Map<String, Location> locationsMap = new HashMap<>();
        locaitons.forEach(l -> locationsMap.put(l.createIdBasedOnCountryAndCity(), l));
        return locationsMap;
    }

    private void updateLocationsWithId(final List<Location> existentLocations,
                                       final Map<String, Location> existentLocationsWithKey) {
        existentLocations.forEach(l -> {
            if (isEmpty(l.getId())) {
                String id = existentLocationsWithKey.get(l.createIdBasedOnCountryAndCity()).getId();
                l.setId(id);
            }
        });
    }

    @Override
    public Location findById(final String id) {
        throwExceptionInCaseOfEmptyId(id);
        Optional<Location> location = repository.findById(id);
        return location.orElseThrow(() -> createAndLogLocationNotFoundException(id));
    }

    private void throwExceptionInCaseOfEmptyId(final String id) {
        if (isEmpty(id)) {
            log.error(NULL_OR_EMPTY_LOCATION_ID);
            throw new LocationServiceException(NULL_OR_EMPTY_LOCATION_ID);
        }
    }

    private LocationServiceException createAndLogLocationNotFoundException(final String id) {
        final String exceptionMessage = String.format(LOCATION_NOT_FOUND, id);
        log.error(exceptionMessage);
        return new LocationServiceException(exceptionMessage);
    }

    @Override
    public List<Location> findAll() {
        return repository.findAll();
    }

    @Override
    public Location update(final Location updatedLocation) {
        boolean validateWithId = true;
        validateLocation(updatedLocation, validateWithId); //todo User may want to update only one location field.
        Location existentLocation = findById(updatedLocation.getId());
        updateExistentLocationFields(existentLocation, updatedLocation);
        return repository.save(updatedLocation);
    }

    private void updateExistentLocationFields(final Location existingLocation, final Location updatedLocation) {
        if (isNotEmpty(updatedLocation.getCountry())) {
            existingLocation.setCountry(updatedLocation.getCountry());
        }
        if (isNotEmpty(updatedLocation.getCity())) {
            existingLocation.setCity(updatedLocation.getCity());
        }
    }

    @Override
    public void delete(final String id) {
        throwExceptionInCaseOfEmptyId(id);
        repository.deleteById(id);
    }

    //todo Field validators might be needed.
}
