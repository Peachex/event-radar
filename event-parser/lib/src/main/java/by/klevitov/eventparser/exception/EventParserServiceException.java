package by.klevitov.eventparser.exception;

public class EventParserServiceException extends Exception {
    public EventParserServiceException() {
        super();
    }

    public EventParserServiceException(String message) {
        super(message);
    }

    public EventParserServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventParserServiceException(Throwable cause) {
        super(cause);
    }

    protected EventParserServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo delete unused constructors.
}
