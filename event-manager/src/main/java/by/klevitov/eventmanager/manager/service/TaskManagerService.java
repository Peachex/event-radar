package by.klevitov.eventmanager.manager.service;

public interface TaskManagerService {
    void executeTask(final String taskIdToExecute);

    //todo Choose strategy for exception handling.
}
