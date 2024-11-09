package by.klevitov.synctaskscheduler.taskscheduler.exception;

public class TaskNotFoundException extends EntityNotFoundException{
    public TaskNotFoundException(String message) {
        super(message);
    }
}
