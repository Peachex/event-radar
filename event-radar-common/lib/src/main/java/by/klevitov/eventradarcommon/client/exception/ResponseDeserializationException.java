package by.klevitov.eventradarcommon.client.exception;

public class ResponseDeserializationException extends RuntimeException {
    public ResponseDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
