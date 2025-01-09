package by.klevitov.eventpersistor.exception;

public abstract class AbstractValidatorException extends RuntimeException {
    public AbstractValidatorException(String message) {
        super(message);
    }
}
