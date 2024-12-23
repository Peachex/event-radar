package by.klevitov.eventmanager.manager.service;

import java.util.List;

public interface TaskManagerService {
    void executeTask(final String taskIdToExecute);

    List<String> retrieveTaskExecutorIds();
}
