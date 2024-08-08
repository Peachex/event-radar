package by.klevitov.eventpersistor.healthcheck.constant;

public final class HealthCheckExceptionMessage {
    public static final String DATABASE_IS_NOT_AVAILABLE = "Database is down: %s";

    private HealthCheckExceptionMessage() {
    }

    //todo Make this class common for the whole application.
}
