package by.klevitov.eventradarcommon.exception;

public abstract class AbstractValidatorException extends RuntimeException {
    public AbstractValidatorException(String message) {
        super(message);
    }
}
