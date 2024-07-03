package by.klevitov.eventparser.exception;

public class ReadingPropertiesFromFileException extends RuntimeException{
    public ReadingPropertiesFromFileException() {
        super();
    }

    public ReadingPropertiesFromFileException(String message) {
        super(message);
    }

    public ReadingPropertiesFromFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadingPropertiesFromFileException(Throwable cause) {
        super(cause);
    }

    protected ReadingPropertiesFromFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo delete unused constructors.
}
