package by.klevitov.eventparser.exception;

public class PriceConversionException extends RuntimeException {
    public PriceConversionException(String message) {
        super(message);
    }

    public PriceConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
