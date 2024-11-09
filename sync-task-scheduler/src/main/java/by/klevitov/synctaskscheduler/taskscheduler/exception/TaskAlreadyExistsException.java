package by.klevitov.synctaskscheduler.taskscheduler.exception;

public class TaskAlreadyExistsException extends EntityAlreadyExistsException{
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
