package by.klevitov.eventmanager.manager.executor;

public interface SyncTaskExecutor {
    void execute();

    String retrieveExecutorId();

    //todo Move duplicated logic to separate class and think about the responsibility of task manager service.
    //todo Think about parameters that may be needed for task execution. May be we need to pass producer message id?
    //todo Think about execution in a separate thread.
}
