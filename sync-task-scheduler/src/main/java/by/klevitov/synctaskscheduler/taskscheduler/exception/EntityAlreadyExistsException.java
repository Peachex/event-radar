package by.klevitov.synctaskscheduler.taskscheduler.exception;

public abstract class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
