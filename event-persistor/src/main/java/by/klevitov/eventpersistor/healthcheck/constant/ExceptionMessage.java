package by.klevitov.eventpersistor.healthcheck.constant;

public final class ExceptionMessage {
    public static final String DATABASE_IS_NOT_AVAILABLE = "Database is down: %s";

    private ExceptionMessage() {
    }

    //todo Make this class common for the whole application.
}
