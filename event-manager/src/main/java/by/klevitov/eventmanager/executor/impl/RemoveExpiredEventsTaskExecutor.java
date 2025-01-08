package by.klevitov.eventmanager.executor.impl;

import by.klevitov.eventmanager.executor.SyncTaskExecutor;
import by.klevitov.eventmanager.service.EventFetcherService;
import by.klevitov.eventmanager.service.EventPersistorClientService;
import by.klevitov.eventmanager.util.TaskExecutorUtil;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.EventDate;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.time.LocalDate.now;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Log4j2
@Component
public class RemoveExpiredEventsTaskExecutor implements SyncTaskExecutor {
    private static final String TASK_NAME = "remove_expired_events_task";
    private static final int MONTHS_AFTER_START_DATE_UNTIL_EVENT_EXPIRES = 6;
    private final EventFetcherService eventFetcherService;
    private final EventPersistorClientService clientService;

    @Autowired
    public RemoveExpiredEventsTaskExecutor(EventFetcherService eventFetcherService,
                                           EventPersistorClientService clientService) {
        this.eventFetcherService = eventFetcherService;
        this.clientService = clientService;
    }

    @Override
    public void execute() {
        try {
            List<AbstractEventDTO> persistentEvents = clientService.findEvents();
            List<AbstractEventDTO> expiredEvents = findExpiredEvents(persistentEvents);
            expiredEvents.forEach(e -> clientService.deleteEvent(e.getId()));
        } catch (Exception e) {
            TaskExecutorUtil.logException(e, TASK_NAME);
        }
    }

    private List<AbstractEventDTO> findExpiredEvents(final List<AbstractEventDTO> events) throws Exception {
        List<AbstractEventDTO> eventsWithNullDate = findEventsWithNullDate(events);
        List<AbstractEventDTO> eventsWithNonNullDate = findEventsWithNonNullDate(events, eventsWithNullDate);
        return (List<AbstractEventDTO>) CollectionUtils.union(
                findExpiredEventsUsingEventsWithNonNullDate(eventsWithNonNullDate),
                findExpiredEventsUsingEventsWithNullDate(eventsWithNullDate));
    }

    private List<AbstractEventDTO> findEventsWithNullDate(final List<AbstractEventDTO> events) {
        Predicate<AbstractEventDTO> eventDateIsNull = e -> e.getDate() == null;
        Predicate<AbstractEventDTO> startDateIsNull = e -> e.getDate().getStartDate() == null;
        Predicate<AbstractEventDTO> endDateIsNull = e -> e.getDate().getEndDate() == null;
        return events.stream()
                .filter(eventDateIsNull.or((startDateIsNull).and(endDateIsNull)))
                .toList();
    }

    private List<AbstractEventDTO> findEventsWithNonNullDate(final List<AbstractEventDTO> events,
                                                             final List<AbstractEventDTO> eventsWithNullDate) {
        return (List<AbstractEventDTO>) CollectionUtils.subtract(events, eventsWithNullDate);
    }

    private List<AbstractEventDTO> findExpiredEventsUsingEventsWithNonNullDate(final List<AbstractEventDTO>
                                                                                       eventsWithNonNullDate) {
        final Predicate<AbstractEventDTO> isEventExpired = createExpiredPredicateForEventsWithDate();
        return eventsWithNonNullDate.stream()
                .filter(isEventExpired)
                .toList();
    }

    private Predicate<AbstractEventDTO> createExpiredPredicateForEventsWithDate() {
        Predicate<AbstractEventDTO> expiredByEndDate = event -> {
            EventDate date = event.getDate();
            return date.getEndDate() != null && now().isAfter(date.getEndDate());
        };
        Predicate<AbstractEventDTO> expiredByStartDate = event -> {
            EventDate date = event.getDate();
            return date.getStartDate() != null && now().minusMonths(MONTHS_AFTER_START_DATE_UNTIL_EVENT_EXPIRES)
                    .isAfter(date.getStartDate());
        };
        return expiredByEndDate.or(expiredByStartDate);
    }

    private List<AbstractEventDTO> findExpiredEventsUsingEventsWithNullDate(final List<AbstractEventDTO>
                                                                                    eventsWithNullDate) throws Exception {
        if (isEmpty(eventsWithNullDate)) {
            return new ArrayList<>();
        }
        final List<AbstractEventDTO> fetchedEvents = eventFetcherService.fetch();
        return (List<AbstractEventDTO>) CollectionUtils.subtract(eventsWithNullDate, fetchedEvents);
    }

    @Override
    public String retrieveExecutorId() {
        return TASK_NAME;
    }
}
