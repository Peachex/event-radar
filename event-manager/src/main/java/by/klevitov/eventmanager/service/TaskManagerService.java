package by.klevitov.eventmanager.service;

import java.util.List;

public interface TaskManagerService {
    void executeTask(final String taskIdToExecute);

    List<String> retrieveTaskExecutorIds();
}
