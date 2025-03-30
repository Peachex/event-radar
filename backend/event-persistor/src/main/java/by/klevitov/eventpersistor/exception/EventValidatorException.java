package by.klevitov.eventpersistor.exception;

import by.klevitov.eventradarcommon.exception.AbstractValidatorException;

public class EventValidatorException extends AbstractValidatorException {
    public EventValidatorException(String message) {
        super(message);
    }
}
