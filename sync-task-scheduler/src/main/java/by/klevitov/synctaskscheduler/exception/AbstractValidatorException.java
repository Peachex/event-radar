package by.klevitov.synctaskscheduler.exception;

public abstract class AbstractValidatorException extends RuntimeException {
    public AbstractValidatorException(String message) {
        super(message);
    }
}
