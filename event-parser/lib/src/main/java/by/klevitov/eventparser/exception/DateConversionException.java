package by.klevitov.eventparser.exception;

public class DateConversionException extends RuntimeException{
    public DateConversionException() {
    }

    public DateConversionException(String message) {
        super(message);
    }

    public DateConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateConversionException(Throwable cause) {
        super(cause);
    }

    //todo delete redundant constructors.
}
