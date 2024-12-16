package by.klevitov.eventmanager.manager.exception;

public class ResponseDeserializationException extends RuntimeException {
    public ResponseDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
