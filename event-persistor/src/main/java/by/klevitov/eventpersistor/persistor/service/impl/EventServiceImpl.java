package by.klevitov.eventpersistor.persistor.service.impl;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.exception.EventServiceException;
import by.klevitov.eventpersistor.persistor.repository.EventRepository;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventpersistor.persistor.util.EventValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.EVENT_NOT_FOUND;
import static by.klevitov.eventpersistor.persistor.util.EventValidator.throwExceptionInCaseOfEmptyId;
import static by.klevitov.eventpersistor.persistor.util.EventValidator.validateEventBeforeCreation;

@Log4j2
@Service
public class EventServiceImpl implements EventService {
    private final LocationService locationService;
    private final EventRepository repository;

    @Autowired
    public EventServiceImpl(LocationService locationService, EventRepository repository) {
        this.locationService = locationService;
        this.repository = repository;
    }

    @Override
    public AbstractEvent create(AbstractEvent event) {
        //todo Add transaction to save event and location in one operation.
        validateEventBeforeCreation(event);
        locationService.create(event.getLocation());
        return repository.findByTitleAndSourceTypeIgnoreCase(event.getTitle(), event.getSourceType())
                .orElseGet(() -> repository.insert(event));
    }

    @Override
    public List<AbstractEvent> create(List<AbstractEvent> events) {
        //todo Add transaction to save event and location in one operation.

        events.forEach(EventValidator::validateEventBeforeCreation);
        List<AbstractEvent> existentEvents = repository.findAll();
        List<AbstractEvent> nonExistentEvents = createNonExistentEventsList(events, existentEvents);
        existentEvents.addAll(repository.saveAll(nonExistentEvents));
        Map<String, AbstractEvent> existentLocationsWithKey = createEventsMapWithTitleAndSourceTypeKey(existentEvents);
        updateEventsWithId(events, existentLocationsWithKey);
        return events;
    }

    private List<AbstractEvent> createNonExistentEventsList(final List<AbstractEvent> events,
                                                            final List<AbstractEvent> existentEvents) {
        List<AbstractEvent> nonExistentEvents = new ArrayList<>();
        Map<String, AbstractEvent> eventsWithKey = createEventsMapWithTitleAndSourceTypeKey(existentEvents);
        events.forEach(e -> {
            String eventKey = e.createIdBasedOnTitleAndSourceType();
            if (!eventsWithKey.containsKey(eventKey)) {
                nonExistentEvents.add(e);
            }
        });
        return nonExistentEvents;
    }

    private Map<String, AbstractEvent> createEventsMapWithTitleAndSourceTypeKey(final List<AbstractEvent> events) {
        Map<String, AbstractEvent> eventsMap = new HashMap<>();
        events.forEach(e -> eventsMap.put(e.createIdBasedOnTitleAndSourceType(), e));
        return eventsMap;
    }

    private void updateEventsWithId(final List<AbstractEvent> events,
                                    final Map<String, AbstractEvent> existentEventsWithKey) {
        events.forEach(e -> e.setId(existentEventsWithKey.get(e.createIdBasedOnTitleAndSourceType()).getId()));
    }

    @Override
    public AbstractEvent findById(String id) {
        throwExceptionInCaseOfEmptyId(id);
        Optional<AbstractEvent> event = repository.findById(id);
        return event.orElseThrow(() -> createAndLogEventNotFoundException(id));
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
    public AbstractEvent update(AbstractEvent updatedEvent) {
        //todo Add transaction to update event and location in one operation.
        //todo AbstractEvent entities will have different fields and should be processed separately. Possible solution
        // is to make this class abstract and create separate non-abstract subclasses for certain event.
        return null;
    }

    @Override
    public void delete(String id) {
        throwExceptionInCaseOfEmptyId(id);
        //todo Add location deletion only for case when location is not used anymore.
        repository.deleteById(id);
    }
}
