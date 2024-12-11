package by.klevitov.eventmanager.manager.executor.impl;

import by.klevitov.eventmanager.manager.client.EventPersistorClient;
import by.klevitov.eventmanager.manager.executor.SyncTaskExecutor;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TestSyncTaskExecutor implements SyncTaskExecutor {
    private static final String executorId = "taskId2";
    private final EventPersistorClient eventPersistorClient;

    @Autowired
    public TestSyncTaskExecutor(EventPersistorClient eventPersistorClient) {
        this.eventPersistorClient = eventPersistorClient;
    }

    @Override
    public void execute() {
        System.out.println(LocalDateTime.now() + ": executing test task.");
        List<AbstractEventDTO> events = eventPersistorClient.getEvents();
        System.out.println(events);
    }

    @Override
    public String retrieveExecutorId() {
        return executorId;
    }

    //todo Delete this class.
}
