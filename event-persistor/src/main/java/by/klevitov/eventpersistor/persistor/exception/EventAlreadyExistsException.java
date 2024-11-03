package by.klevitov.eventpersistor.persistor.exception;

public class EventAlreadyExistsException extends EntityAlreadyExistsException {
    public EventAlreadyExistsException(String message) {
        super(message);
    }
}
