package by.klevitov.synctaskscheduler.exception;

public class TaskAlreadyExistsException extends EntityAlreadyExistsException{
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
