package by.klevitov.eventpersistor.persistor.exception;

public abstract class AbstractValidatorException extends RuntimeException {
    public AbstractValidatorException(String message) {
        super(message);
    }
}
