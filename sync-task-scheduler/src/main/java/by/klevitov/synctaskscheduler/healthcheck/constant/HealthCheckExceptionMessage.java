package by.klevitov.synctaskscheduler.healthcheck.constant;

public final class HealthCheckExceptionMessage {
    public static final String DATABASE_IS_NOT_AVAILABLE = "Database is down: %s";
    public static final String MESSAGE_BROKER_IS_NOT_AVAILABLE = "Message broker is down: %s:%s";

    private HealthCheckExceptionMessage() {
    }
}
