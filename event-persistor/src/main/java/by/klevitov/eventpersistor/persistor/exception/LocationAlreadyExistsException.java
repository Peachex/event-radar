package by.klevitov.eventpersistor.persistor.exception;

public class LocationAlreadyExistsException extends EntityAlreadyExistsException{
    public LocationAlreadyExistsException(String message) {
        super(message);
    }
}
