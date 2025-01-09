package by.klevitov.synctaskscheduler.web.handler;

import by.klevitov.eventradarcommon.exception.ExceptionResponse;
import by.klevitov.synctaskscheduler.exception.AbstractValidatorException;
import by.klevitov.synctaskscheduler.exception.EntityAlreadyExistsException;
import by.klevitov.synctaskscheduler.exception.EntityNotFoundException;
import by.klevitov.synctaskscheduler.exception.SchedulerServiceException;
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

    @ExceptionHandler(EntityAlreadyExistsException.class)
    protected ResponseEntity<ExceptionResponse> handleEntityConflictException(final EntityAlreadyExistsException e) {
        final HttpStatus httpStatus = HttpStatus.CONFLICT;
        ExceptionResponse exceptionResponse = new ExceptionResponse(httpStatus.value(), e);
        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }

    @ExceptionHandler(SchedulerServiceException.class)
    protected ResponseEntity<ExceptionResponse> handleSchedulerServiceException(final SchedulerServiceException e) {
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponse exceptionResponse = new ExceptionResponse(httpStatus.value(), e);
        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }
}
