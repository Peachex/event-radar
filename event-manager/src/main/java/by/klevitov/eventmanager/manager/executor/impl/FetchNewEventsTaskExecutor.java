package by.klevitov.eventmanager.manager.executor.impl;

import by.klevitov.eventmanager.manager.executor.SyncTaskExecutor;
import by.klevitov.eventmanager.manager.service.EventFetcherService;
import by.klevitov.eventmanager.manager.service.EventPersistorClientService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.klevitov.eventmanager.manager.util.TaskExecutorUtil.logException;

@Log4j2
@Component
public class FetchNewEventsTaskExecutor implements SyncTaskExecutor {
    private static final String TASK_NAME = "fetch_new_events_task";
    private final EventFetcherService eventFetcherService;
    private final EventPersistorClientService clientService;

    @Autowired
    public FetchNewEventsTaskExecutor(EventFetcherService eventFetcherService,
                                      EventPersistorClientService clientService) {
        this.eventFetcherService = eventFetcherService;
        this.clientService = clientService;
    }

    @Override
    public void execute() {
        try {
            List<AbstractEventDTO> fetchedEvents = eventFetcherService.fetch();
            clientService.createEvents(fetchedEvents);
        } catch (Exception e) {
            logException(e, TASK_NAME);
        }
    }

    @Override
    public String retrieveExecutorId() {
        return TASK_NAME;
    }
}
