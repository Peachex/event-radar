package by.klevitov.eventpersistor.persistor.exception;

public abstract class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
