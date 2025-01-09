package by.klevitov.synctaskscheduler.exception;

public abstract class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
