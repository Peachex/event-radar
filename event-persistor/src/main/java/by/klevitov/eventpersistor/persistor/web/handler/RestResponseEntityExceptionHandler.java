package by.klevitov.eventpersistor.persistor.web.handler;

import by.klevitov.eventpersistor.persistor.exception.AbstractValidatorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AbstractValidatorException.class)
    protected ResponseEntity<ExceptionResponse> handleValidatorException(AbstractValidatorException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), e);
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    //todo Verify that handleValidatorException also will work for EventValidator exception.
//    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
}
