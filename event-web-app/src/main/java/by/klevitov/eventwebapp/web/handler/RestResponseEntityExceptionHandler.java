package by.klevitov.eventwebapp.web.handler;

import by.klevitov.eventradarcommon.client.exception.EventPersistorClientException;
import by.klevitov.eventradarcommon.client.exception.ResponseDeserializationException;
import by.klevitov.eventradarcommon.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EventPersistorClientException.class)
    protected ResponseEntity<ExceptionResponse> handleClientException(final EventPersistorClientException e) {
        final ExceptionResponse exceptionResponse = e.getExceptionResponse();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(ResponseDeserializationException.class)
    protected ResponseEntity<ExceptionResponse> handleClientException(final ResponseDeserializationException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
}
