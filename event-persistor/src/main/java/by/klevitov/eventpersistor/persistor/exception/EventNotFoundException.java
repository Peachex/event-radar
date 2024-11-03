package by.klevitov.eventpersistor.persistor.exception;

public class EventNotFoundException extends EntityNotFoundException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
