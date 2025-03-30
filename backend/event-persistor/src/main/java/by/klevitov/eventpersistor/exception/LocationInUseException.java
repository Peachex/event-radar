package by.klevitov.eventpersistor.exception;

public class LocationInUseException extends RuntimeException {
    public LocationInUseException(String message) {
        super(message);
    }
}
