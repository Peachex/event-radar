package by.klevitov.eventpersistor.persistor.exception;

public class InvalidEventClassException extends RuntimeException {
    public InvalidEventClassException(String message) {
        super(message);
    }
}
