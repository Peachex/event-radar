package by.klevitov.eventpersistor.persistor.exception;

public class LocationValidatorException extends AbstractValidatorException{
    public LocationValidatorException() {
    }

    public LocationValidatorException(String message) {
        super(message);
    }

    public LocationValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocationValidatorException(Throwable cause) {
        super(cause);
    }

    public LocationValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo Delete redundant constructors.
}
