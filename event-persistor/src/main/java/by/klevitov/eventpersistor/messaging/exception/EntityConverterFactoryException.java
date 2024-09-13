package by.klevitov.eventpersistor.messaging.exception;

public class EntityConverterFactoryException extends RuntimeException {
    public EntityConverterFactoryException() {
    }

    public EntityConverterFactoryException(String message) {
        super(message);
    }

    public EntityConverterFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityConverterFactoryException(Throwable cause) {
        super(cause);
    }

    public EntityConverterFactoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo Delete redundant constructors.
}
