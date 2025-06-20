package by.klevitov.eventpersistor.web.handler;

import by.klevitov.eventpersistor.exception.EntityAlreadyExistsException;
import by.klevitov.eventpersistor.exception.EntityConverterException;
import by.klevitov.eventpersistor.exception.EntityNotFoundException;
import by.klevitov.eventpersistor.exception.LocationInUseException;
import by.klevitov.eventradarcommon.exception.AbstractValidatorException;
import by.klevitov.eventradarcommon.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AbstractValidatorException.class)
    protected ResponseEntity<ExceptionResponse> handleValidatorException(final AbstractValidatorException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), e);
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ExceptionResponse> handleEntityNotFoundException(final EntityNotFoundException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(value = {EntityAlreadyExistsException.class, LocationInUseException.class})
    protected ResponseEntity<ExceptionResponse> handleEntityConflictException(final RuntimeException e) {
        final HttpStatus httpStatus = HttpStatus.CONFLICT;
        ExceptionResponse exceptionResponse = new ExceptionResponse(httpStatus.value(), e);
        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }

    @ExceptionHandler(value = EntityConverterException.class)
    protected ResponseEntity<ExceptionResponse> handleEntityConverterException(final EntityConverterException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), e);
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(value = Throwable.class)
    protected ResponseEntity<ExceptionResponse> handleUncaughtException(final Throwable e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }
}
