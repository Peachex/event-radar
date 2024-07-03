package by.klevitov.eventparser.exception;

public class HTMLDocumentRetrievingException extends Exception{
    public HTMLDocumentRetrievingException() {
        super();
    }

    public HTMLDocumentRetrievingException(String message) {
        super(message);
    }

    public HTMLDocumentRetrievingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HTMLDocumentRetrievingException(Throwable cause) {
        super(cause);
    }

    protected HTMLDocumentRetrievingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //todo delete unused constructors.
}
