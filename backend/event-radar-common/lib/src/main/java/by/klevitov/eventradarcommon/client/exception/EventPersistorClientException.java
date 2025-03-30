package by.klevitov.eventradarcommon.client.exception;

import by.klevitov.eventradarcommon.exception.ExceptionResponse;
import lombok.Getter;

@Getter
public class EventPersistorClientException extends RuntimeException {
    private final ExceptionResponse exceptionResponse;

    public EventPersistorClientException(ExceptionResponse exceptionResponse, String message) {
        super(message);
        this.exceptionResponse = exceptionResponse;
    }
}
