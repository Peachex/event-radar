package by.klevitov.synctaskscheduler.taskscheduler.exception;

public abstract class AbstractValidatorException extends RuntimeException {
    public AbstractValidatorException(String message) {
        super(message);
    }
}
