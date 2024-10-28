package by.klevitov.eventpersistor.persistor.exception;

public class LocationInUseException extends RuntimeException {
    public LocationInUseException(String message) {
        super(message);
    }
}
