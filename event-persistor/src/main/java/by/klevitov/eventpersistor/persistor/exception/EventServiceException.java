package by.klevitov.eventpersistor.persistor.exception;

public class EventServiceException extends AbstractServiceException {
    public EventServiceException() {
    }

    public EventServiceException(String message) {
        super(message);
    }

    public EventServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventServiceException(Throwable cause) {
        super(cause);
    }

    public EventServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo Delete redundant constructors.
}
