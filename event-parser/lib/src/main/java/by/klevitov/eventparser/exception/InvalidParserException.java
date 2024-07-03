package by.klevitov.eventparser.exception;

public class InvalidParserException extends RuntimeException{
    public InvalidParserException() {
        super();
    }

    public InvalidParserException(String message) {
        super(message);
    }

    public InvalidParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParserException(Throwable cause) {
        super(cause);
    }

    protected InvalidParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo delete unused constructors.
}
