package by.klevitov.eventpersistor.exception;

public class EventNotFoundException extends EntityNotFoundException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
