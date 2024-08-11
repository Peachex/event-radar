package by.klevitov.eventpersistor.persistor.service.impl;

import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.exception.LocationServiceException;
import by.klevitov.eventpersistor.persistor.repository.LocationRepository;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventpersistor.persistor.util.LocationValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.LOCATION_ALREADY_EXISTS;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.LOCATION_NOT_FOUND;
import static by.klevitov.eventpersistor.persistor.util.LocationValidator.throwExceptionInCaseOfEmptyId;
import static by.klevitov.eventpersistor.persistor.util.LocationValidator.validateLocationBeforeCreation;
import static by.klevitov.eventpersistor.persistor.util.LocationValidator.validateLocationBeforeUpdating;
import static org.apache.commons.lang3.StringUtils.isEmpty;

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
        validateLocationBeforeCreation(location);
        return repository.findByCountryAndCityIgnoreCase(location.getCountry(), location.getCity())
                .orElseGet(() -> repository.insert(location));
    }

    @Override
    public List<Location> createMultiple(final List<Location> locations) {
        locations.forEach(LocationValidator::validateLocationBeforeCreation);
        List<Location> existentLocations = repository.findAll();
        List<Location> nonExistentLocations = createNonExistentLocationsList(locations, existentLocations);
        existentLocations.addAll(repository.saveAll(nonExistentLocations));
        Map<String, Location> existentLocationsWithKey = createLocationsMapWithCountryCityKey(existentLocations);
        updateLocationsWithId(locations, existentLocationsWithKey);
        return locations;
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

    private void updateLocationsWithId(final List<Location> locations,
                                       final Map<String, Location> existentLocationsWithKey) {
        locations.forEach(l -> l.setId(existentLocationsWithKey.get(l.createIdBasedOnCountryAndCity()).getId()));
    }

    @Override
    public Location findById(final String id) {
        throwExceptionInCaseOfEmptyId(id);
        Optional<Location> location = repository.findById(id);
        return location.orElseThrow(() -> createAndLogLocationNotFoundException(id));
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
        validateLocationBeforeUpdating(updatedLocation);
        Location existentLocation = findById(updatedLocation.getId());
        updateLocationWithExistentFields(updatedLocation, existentLocation);
        throwExceptionInCaseOfLocationAlreadyExists(updatedLocation);
        return repository.save(updatedLocation);
    }

    private void updateLocationWithExistentFields(final Location updatedLocation, final Location existingLocation) {
        if (isEmpty(updatedLocation.getCountry())) {
            updatedLocation.setCountry(existingLocation.getCountry());
        }
        if (isEmpty(updatedLocation.getCity())) {
            updatedLocation.setCity(existingLocation.getCity());
        }
    }

    private void throwExceptionInCaseOfLocationAlreadyExists(final Location location) {
        final String country = location.getCountry();
        final String city = location.getCity();
        final String id = location.getId();
        if (repository.findByCountryAndCityIgnoreCase(country, city).isPresent()) {
            final String exceptionMessage = String.format(LOCATION_ALREADY_EXISTS, country, city, id);
            log.error(exceptionMessage);
            throw new LocationServiceException(exceptionMessage);
        }
    }

    @Override
    public void delete(final String id) {
        throwExceptionInCaseOfEmptyId(id);
        repository.deleteById(id);
    }
}
