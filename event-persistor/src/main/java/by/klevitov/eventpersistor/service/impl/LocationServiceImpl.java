package by.klevitov.eventpersistor.service.impl;

import by.klevitov.eventpersistor.common.dto.PageRequestDTO;
import by.klevitov.eventpersistor.exception.LocationAlreadyExistsException;
import by.klevitov.eventpersistor.exception.LocationInUseException;
import by.klevitov.eventpersistor.exception.LocationNotFoundException;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.repository.EventMongoRepository;
import by.klevitov.eventpersistor.repository.LocationMongoRepository;
import by.klevitov.eventpersistor.service.LocationService;
import by.klevitov.eventpersistor.util.LocationValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static by.klevitov.eventpersistor.common.util.PageRequestValidator.validatePageRequest;
import static by.klevitov.eventpersistor.constant.PersistorExceptionMessage.LOCATION_ALREADY_EXISTS;
import static by.klevitov.eventpersistor.constant.PersistorExceptionMessage.LOCATION_IS_IN_USE;
import static by.klevitov.eventpersistor.constant.PersistorExceptionMessage.LOCATION_NOT_FOUND;
import static by.klevitov.eventpersistor.util.LocationValidator.throwExceptionInCaseOfEmptyId;
import static by.klevitov.eventpersistor.util.LocationValidator.validateLocationBeforeCreation;
import static by.klevitov.eventpersistor.util.LocationValidator.validateLocationBeforeUpdating;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

@Log4j2
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationMongoRepository locationRepository;
    private final EventMongoRepository eventRepository;

    @Autowired
    public LocationServiceImpl(LocationMongoRepository locationRepository, EventMongoRepository eventRepository) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Location create(final Location location) {
        validateLocationBeforeCreation(location);
        return createLocationOrGetExistingOne(location);
    }

    private Location createLocationOrGetExistingOne(final Location location) {
        return locationRepository.findByCountryAndCityIgnoreCase(location.getCountry(), location.getCity())
                .orElseGet(() -> locationRepository.insert(location));
    }

    @Override
    public List<Location> create(final List<Location> locations) {
        locations.forEach(LocationValidator::validateLocationBeforeCreation);
        return createLocationsWithoutDuplication(locations);
    }

    private List<Location> createLocationsWithoutDuplication(final List<Location> locations) {
        List<Location> existentLocations = locationRepository.findByCountryAndCityIgnoreCase(locations);
        List<Location> nonExistentLocations = createNonExistentLocationsList(locations, existentLocations);
        existentLocations.addAll(locationRepository.saveAll(nonExistentLocations));
        Map<String, Location> existentLocationsWithKey = createLocationsMapWithCountryCityKey(existentLocations);
        updateLocationsWithId(locations, existentLocationsWithKey);
        return locations;
    }

    private List<Location> createNonExistentLocationsList(final List<Location> locations,
                                                          final List<Location> existentLocations) {
        Set<Location> nonExistentLocations = new HashSet<>();
        Map<String, Location> locationsWithKey = createLocationsMapWithCountryCityKey(existentLocations);
        locations.forEach(l -> {
            String locationKey = l.createIdBasedOnCountryAndCity();
            if (!locationsWithKey.containsKey(locationKey)) {
                nonExistentLocations.add(l);
            }
        });
        return nonExistentLocations.stream().toList();
    }

    private Map<String, Location> createLocationsMapWithCountryCityKey(final List<Location> locations) {
        Map<String, Location> locationsMap = new HashMap<>();
        locations.forEach(l -> locationsMap.put(l.createIdBasedOnCountryAndCity(), l));
        return locationsMap;
    }

    private void updateLocationsWithId(final List<Location> locations,
                                       final Map<String, Location> existentLocationsWithKey) {
        locations.forEach(l -> l.setId(existentLocationsWithKey.get(l.createIdBasedOnCountryAndCity()).getId()));
    }

    @Override
    public Location findById(final String id) {
        throwExceptionInCaseOfEmptyId(id);
        Optional<Location> location = locationRepository.findById(id);
        return location.orElseThrow(() -> createAndLogLocationNotFoundException(id));
    }

    @Override
    public List<Location> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch) {
        return (isNotEmpty(fields) ? locationRepository.findByFields(fields, isCombinedMatch) : new ArrayList<>());
    }

    @Override
    public Page<Location> findByFields(Map<String, Object> fields, boolean isCombinedMatch,
                                       final PageRequestDTO pageRequestDTO) {
        //todo update tests to include validatePageRequest feat.
        validatePageRequest(pageRequestDTO);
        return (isNotEmpty(fields)
                ? locationRepository.findByFields(fields, isCombinedMatch, pageRequestDTO.createPageRequest())
                : new PageImpl<>(new ArrayList<>()));
    }

    private LocationNotFoundException createAndLogLocationNotFoundException(final String id) {
        final String exceptionMessage = String.format(LOCATION_NOT_FOUND, id);
        log.error(exceptionMessage);
        return new LocationNotFoundException(exceptionMessage);
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Page<Location> findAll(final PageRequestDTO pageRequestDTO) {
        //todo update tests to include validatePageRequest feat.
        validatePageRequest(pageRequestDTO);
        return locationRepository.findAll(pageRequestDTO.createPageRequest());
    }

    @Override
    public Location update(final Location updatedLocation) {
        validateLocationBeforeUpdating(updatedLocation);
        Location existentLocation = findById(updatedLocation.getId());
        updatedLocation.copyValuesForNullOrEmptyFieldsFromLocation(existentLocation);
        throwExceptionInCaseOfLocationAlreadyExists(updatedLocation);
        return locationRepository.save(updatedLocation);
    }

    private void throwExceptionInCaseOfLocationAlreadyExists(final Location updatedLocation) {
        if (updatedLocationAlreadyExists(updatedLocation)) {
            final String exceptionMessage = String.format(LOCATION_ALREADY_EXISTS, updatedLocation.getCountry(),
                    updatedLocation.getCity(), updatedLocation.getId());
            log.error(exceptionMessage);
            throw new LocationAlreadyExistsException(exceptionMessage);
        }
    }

    private boolean updatedLocationAlreadyExists(final Location updatedLocation) {
        final Optional<Location> existentLocation = locationRepository.findByCountryAndCityIgnoreCase(
                updatedLocation.getCountry(), updatedLocation.getCity());
        return (existentLocation.isPresent() && !existentLocation.get().getId().equals(updatedLocation.getId()));
    }

    @Override
    public void delete(final String id) {
        Location location = findById(id);
        throwExceptionInCaseOfLocationIsInUse(location);
        locationRepository.deleteById(id);
    }

    private void throwExceptionInCaseOfLocationIsInUse(final Location location) {
        if (eventRepository.countByLocation(location) != 0) {
            final String exceptionMessage = String.format(LOCATION_IS_IN_USE, location.getId());
            log.error(exceptionMessage);
            throw new LocationInUseException(exceptionMessage);
        }
    }
}
