package by.klevitov.eventmanager.executor;

public interface SyncTaskExecutor {
    void execute();

    String retrieveExecutorId();
}
