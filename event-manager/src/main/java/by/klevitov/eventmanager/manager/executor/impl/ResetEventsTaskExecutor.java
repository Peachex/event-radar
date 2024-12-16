package by.klevitov.eventmanager.manager.executor.impl;

import by.klevitov.eventmanager.manager.client.EventPersistorClient;
import by.klevitov.eventmanager.manager.executor.SyncTaskExecutor;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class ResetEventsTaskExecutor implements SyncTaskExecutor {
    private static final String TASK_NAME = "reset_events_task";
    private final EventParserService eventParserService;
    private final EventPersistorClient persistorClient;

    @Autowired
    public ResetEventsTaskExecutor(EventParserService eventParserService, EventPersistorClient persistorClient) {
        this.eventParserService = eventParserService;
        this.persistorClient = persistorClient;
    }

    @Override
    public void execute() {
        try {
            deleteAllEvents();
            List<AbstractEventDTO> fetchedEvents = fetchEvents();
            persistorClient.create(fetchedEvents);
        } catch (Exception e) {
            final String exceptionMessage = String.format("An error occurs while executing: '%s'. Exception message: "
                    + "'%s'.", TASK_NAME, e.getMessage());
            log.error(exceptionMessage, e);
        }
    }

    //todo Add deleteAll method to event persistor and then replace all calls with single request.
    private void deleteAllEvents() {
        List<AbstractEventDTO> existentEvents = persistorClient.findAll();
        existentEvents.forEach(e -> persistorClient.delete(e.getId()));
    }

    private List<AbstractEventDTO> fetchEvents() throws Exception {
        final List<AbstractEventDTO> events = new ArrayList<>();
        for (EventParser parser : eventParserService.retrieveAvailableParsers().values()) {
            events.addAll(eventParserService.retrieveEvents(parser));
        }
        return events;
    }

    @Override
    public String retrieveExecutorId() {
        return TASK_NAME;
    }
}
