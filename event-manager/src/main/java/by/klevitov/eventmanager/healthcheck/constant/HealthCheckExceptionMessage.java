package by.klevitov.eventmanager.healthcheck.constant;

public final class HealthCheckExceptionMessage {
    public static final String MESSAGE_BROKER_IS_NOT_AVAILABLE = "Message broker is down: %s:%s";
    public static final String EVENT_PERSISTOR_IS_NOT_AVAILABLE = "Event persistor module is down. Error message: '%s'.";

    private HealthCheckExceptionMessage() {
    }
}
