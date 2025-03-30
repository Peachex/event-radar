package by.klevitov.eventpersistor.exception;

public abstract class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
