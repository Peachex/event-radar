package by.klevitov.eventpersistor.persistor.exception;

public abstract class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
