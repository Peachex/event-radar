package by.klevitov.eventpersistor.messaging.exception;

public class EntityConverterException extends RuntimeException{
    public EntityConverterException() {
    }

    public EntityConverterException(String message) {
        super(message);
    }

    public EntityConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityConverterException(Throwable cause) {
        super(cause);
    }

    public EntityConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo Delete redundant constructors.
}
