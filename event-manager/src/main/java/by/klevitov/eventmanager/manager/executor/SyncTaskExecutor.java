package by.klevitov.eventmanager.manager.executor;

public interface SyncTaskExecutor {
    void execute();

    String retrieveExecutorId();

    //todo Think about execution in a separate thread.
}
