package by.klevitov.eventpersistor.persistor.exception;

public class LocationServiceException extends RuntimeException{
    public LocationServiceException() {
    }

    public LocationServiceException(String message) {
        super(message);
    }

    public LocationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocationServiceException(Throwable cause) {
        super(cause);
    }

    public LocationServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo Delete redundant constructors.
}
