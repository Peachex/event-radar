package by.klevitov.eventparser.exception;

public class DateConversionException extends RuntimeException{
    public DateConversionException(String message) {
        super(message);
    }

    public DateConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
