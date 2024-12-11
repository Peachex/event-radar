package by.klevitov.eventmanager.manager.executor.impl;

import by.klevitov.eventmanager.manager.client.EventPersistorClient;
import by.klevitov.eventmanager.manager.executor.SyncTaskExecutor;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnotherTestTaskExecutor implements SyncTaskExecutor {
    private final EventPersistorClient eventPersistorClient;

    @Autowired
    public AnotherTestTaskExecutor(EventPersistorClient eventPersistorClient) {
        this.eventPersistorClient = eventPersistorClient;
    }

    @Override
    public void execute() {
        System.out.println(LocalDateTime.now() + ": executing another test task.");
        AbstractEventDTO event = eventPersistorClient.getById("66e4a6a352a245767fcefc2e_");
        System.out.println(event);
    }

    @Override
    public String retrieveExecutorId() {
        return "testTask";
    }
}
