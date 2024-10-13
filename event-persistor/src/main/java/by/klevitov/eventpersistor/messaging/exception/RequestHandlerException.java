package by.klevitov.eventpersistor.messaging.exception;

public class RequestHandlerException extends RuntimeException {
    public RequestHandlerException(String message) {
        super(message);
    }
}
