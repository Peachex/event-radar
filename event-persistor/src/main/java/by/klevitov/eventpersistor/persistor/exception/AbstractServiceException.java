package by.klevitov.eventpersistor.persistor.exception;

public abstract class AbstractServiceException extends RuntimeException {
    public AbstractServiceException() {
    }

    public AbstractServiceException(String message) {
        super(message);
    }

    public AbstractServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractServiceException(Throwable cause) {
        super(cause);
    }

    public AbstractServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo Delete redundant constructors.
}
