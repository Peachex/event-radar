package by.klevitov.eventpersistor.persistor.exception;

public abstract class AbstractServiceException extends RuntimeException {
    public AbstractServiceException(String message) {
        super(message);
    }
}
