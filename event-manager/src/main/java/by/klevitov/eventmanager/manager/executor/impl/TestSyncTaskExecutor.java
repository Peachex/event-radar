package by.klevitov.eventmanager.manager.executor.impl;

import by.klevitov.eventmanager.manager.executor.SyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestSyncTaskExecutor implements SyncTaskExecutor {
    private static final String executorId = "taskId2";

    @Override
    public void execute() {
        System.out.println(LocalDateTime.now() + ": executing test task.");
    }

    @Override
    public String retrieveExecutorId() {
        return executorId;
    }

    //todo Delete this class.
}
