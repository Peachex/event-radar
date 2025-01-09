package by.klevitov.synctaskscheduler.exception;

public class TaskNotFoundException extends EntityNotFoundException{
    public TaskNotFoundException(String message) {
        super(message);
    }
}
