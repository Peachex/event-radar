package by.klevitov.eventmanager.manager.executor;

public interface SyncTaskExecutor {
    void execute();

    String retrieveExecutorId();

    //todo Think about parameters that may be needed for task execution. May be we need to pass producer message id?
    //todo Think about exceptions.
    //todo Think about execution in a separate thread.
}
