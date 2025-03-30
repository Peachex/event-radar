package by.klevitov.eventpersistor.exception;

public class EventAlreadyExistsException extends EntityAlreadyExistsException {
    public EventAlreadyExistsException(String message) {
        super(message);
    }
}
