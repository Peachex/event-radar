package by.klevitov.eventpersistor.persistor.exception;

public class EventValidatorException extends AbstractValidatorException{
    public EventValidatorException() {
    }

    public EventValidatorException(String message) {
        super(message);
    }

    public EventValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventValidatorException(Throwable cause) {
        super(cause);
    }

    public EventValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo Delete redundant constructors.
}
