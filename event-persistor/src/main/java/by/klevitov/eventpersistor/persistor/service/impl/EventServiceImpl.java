package by.klevitov.eventpersistor.persistor.service.impl;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.exception.EventServiceException;
import by.klevitov.eventpersistor.persistor.repository.EventMongoRepository;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventpersistor.persistor.util.EventValidator;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.EVENT_ALREADY_EXISTS;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.EVENT_NOT_FOUND;
import static by.klevitov.eventpersistor.persistor.util.EventValidator.throwExceptionInCaseOfEmptyId;
import static by.klevitov.eventpersistor.persistor.util.EventValidator.validateEventBeforeCreation;
import static by.klevitov.eventpersistor.persistor.util.EventValidator.validateEventBeforeUpdating;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

@Log4j2
@Service
public class EventServiceImpl implements EventService {
    private final LocationService locationService;
    private final EventMongoRepository repository;

    @Autowired
    public EventServiceImpl(LocationService locationService, EventMongoRepository repository) {
        this.locationService = locationService;
        this.repository = repository;
    }

    //@Transactional
    @Override
    public AbstractEvent create(final AbstractEvent event) {
        validateEventBeforeCreation(event);
        processLocationCreation(event);
        return createEventOrGetExistingOne(event);
    }

    private void processLocationCreation(final AbstractEvent event) {
        Location locationWithId = locationService.create(event.getLocation());
        event.setLocation(locationWithId);
    }

    private AbstractEvent createEventOrGetExistingOne(final AbstractEvent event) {
        return repository.findFirstByTitleAndCategoryIgnoreCaseAndSourceType(
                        event.getTitle(),
                        event.getCategory(),
                        event.getSourceType())
                .orElseGet(() -> repository.insert(event));
    }

    //@Transactional
    @Override
    public List<AbstractEvent> create(final List<AbstractEvent> events) {
        //todo Add transaction to save event and location in one operation.
        events.forEach(EventValidator::validateEventBeforeCreation);
        processLocationsCreation(events);
        return createEventsWithoutDuplication(events);
    }

    private void processLocationsCreation(final List<AbstractEvent> events) {
        List<Location> locations = events.stream().map(AbstractEvent::getLocation).toList();
        locationService.create(locations);
    }

    private List<AbstractEvent> createEventsWithoutDuplication(final List<AbstractEvent> events) {
        List<AbstractEvent> existentEvents = repository.findAll();
        List<AbstractEvent> nonExistentEvents = createNonExistentEventsList(events, existentEvents);
        existentEvents.addAll(repository.saveAll(nonExistentEvents));
        Map<String, AbstractEvent> existentEventsWithKey = createEventsMapWithTitleAndSourceTypeKey(existentEvents);
        updateEventsWithId(events, existentEventsWithKey);
        return events;
    }

    private List<AbstractEvent> createNonExistentEventsList(final List<AbstractEvent> events,
                                                            final List<AbstractEvent> existentEvents) {
        Set<AbstractEvent> nonExistentEvents = new HashSet<>();
        Map<String, AbstractEvent> eventsWithKey = createEventsMapWithTitleAndSourceTypeKey(existentEvents);
        events.forEach(e -> {
            String eventKey = e.createKeyForComparing();
            if (!eventsWithKey.containsKey(eventKey)) {
                nonExistentEvents.add(e);
            }
        });
        return nonExistentEvents.stream().toList();
    }

    private Map<String, AbstractEvent> createEventsMapWithTitleAndSourceTypeKey(final List<AbstractEvent> events) {
        Map<String, AbstractEvent> eventsMap = new HashMap<>();
        events.forEach(e -> eventsMap.put(e.createKeyForComparing(), e));
        return eventsMap;
    }

    private void updateEventsWithId(final List<AbstractEvent> events,
                                    final Map<String, AbstractEvent> existentEventsWithKey) {
        events.forEach(e -> e.setId(existentEventsWithKey.get(e.createKeyForComparing()).getId()));
    }

    @Override
    public AbstractEvent findById(final String id) {
        throwExceptionInCaseOfEmptyId(id);
        Optional<AbstractEvent> event = repository.findById(id);
        return event.orElseThrow(() -> createAndLogEventNotFoundException(id));
    }

    @Override
    public List<AbstractEvent> findByFields(Map<String, Object> fields) {
        return (isNotEmpty(fields) ? repository.findByFields(fields) : new ArrayList<>());
    }

    private EventServiceException createAndLogEventNotFoundException(final String id) {
        final String exceptionMessage = String.format(EVENT_NOT_FOUND, id);
        log.error(exceptionMessage);
        return new EventServiceException(exceptionMessage);
    }

    @Override
    public List<AbstractEvent> findAll() {
        return repository.findAll();
    }

    @Override
    public AbstractEvent update(final AbstractEvent updatedEvent) {
        //todo Add transaction to update event and location in one operation.
        validateEventBeforeUpdating(updatedEvent);
        AbstractEvent existentEvent = findById(updatedEvent.getId());
        updatedEvent.copyValuesForNullOrEmptyFieldsFromEvent(existentEvent);
        processLocationCreation(updatedEvent);
        throwExceptionInCaseOfEventAlreadyExists(updatedEvent);
        return repository.save(updatedEvent);
    }

    private void throwExceptionInCaseOfEventAlreadyExists(final AbstractEvent event) {
        final String title = event.getTitle();
        final String category = event.getCategory();
        final EventSourceType sourceType = event.getSourceType();
        final String id = event.getId();
        if (repository.findFirstByTitleAndCategoryIgnoreCaseAndSourceType(title, category, sourceType).isPresent()) {
            final String exceptionMessage = String.format(EVENT_ALREADY_EXISTS, title, category, sourceType, id);
            log.error(exceptionMessage);
            throw new EventServiceException(exceptionMessage);
        }
    }

    @Override
    public void delete(final String id) {
        findById(id);
        repository.deleteById(id);
    }
}