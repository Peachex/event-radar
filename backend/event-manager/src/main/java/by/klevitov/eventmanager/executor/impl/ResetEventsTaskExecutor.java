package by.klevitov.eventmanager.executor.impl;

import by.klevitov.coordinateresolver.service.CoordinateResolverService;
import by.klevitov.eventmanager.executor.SyncTaskExecutor;
import by.klevitov.eventmanager.service.EventFetcherService;
import by.klevitov.eventmanager.service.EventPersistorClientService;
import by.klevitov.eventmanager.util.TaskExecutorUtil;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.klevitov.eventmanager.util.TaskExecutorUtil.resolveCoordinatesIfNeeded;

@Log4j2
@Component
public class ResetEventsTaskExecutor implements SyncTaskExecutor {
    private static final String TASK_NAME = "reset_events_task";
    private final EventFetcherService eventFetcherService;
    private final EventPersistorClientService clientService;
    private final CoordinateResolverService coordinateResolver;

    @Autowired
    public ResetEventsTaskExecutor(EventFetcherService eventFetcherService, EventPersistorClientService clientService,
                                   CoordinateResolverService coordinateResolver) {
        this.eventFetcherService = eventFetcherService;
        this.clientService = clientService;
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public void execute() {
        try {
            clientService.deleteEvents();
            List<AbstractEventDTO> fetchedEvents = eventFetcherService.fetch();
            resolveCoordinatesIfNeeded(fetchedEvents, coordinateResolver);
            clientService.createEvents(fetchedEvents);
        } catch (Exception e) {
            TaskExecutorUtil.logException(e, TASK_NAME);
        }
    }

    @Override
    public String retrieveExecutorId() {
        return TASK_NAME;
    }
}
