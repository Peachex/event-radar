package by.klevitov.eventpersistor.exception;

public class InvalidEventClassException extends RuntimeException {
    public InvalidEventClassException(String message) {
        super(message);
    }
}
