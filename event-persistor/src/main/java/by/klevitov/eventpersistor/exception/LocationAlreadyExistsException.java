package by.klevitov.eventpersistor.exception;

public class LocationAlreadyExistsException extends EntityAlreadyExistsException{
    public LocationAlreadyExistsException(String message) {
        super(message);
    }
}
