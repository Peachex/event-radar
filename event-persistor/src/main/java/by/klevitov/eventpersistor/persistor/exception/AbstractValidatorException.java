package by.klevitov.eventpersistor.persistor.exception;

public abstract class AbstractValidatorException extends RuntimeException{
    public AbstractValidatorException() {
    }

    public AbstractValidatorException(String message) {
        super(message);
    }

    public AbstractValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractValidatorException(Throwable cause) {
        super(cause);
    }

    public AbstractValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo Delete redundant constructors.
}
