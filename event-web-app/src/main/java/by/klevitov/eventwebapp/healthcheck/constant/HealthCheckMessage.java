package by.klevitov.eventwebapp.healthcheck.constant;

public final class HealthCheckMessage {
    public static final String APPLICATION_HEALTH_KEY = "application_status";
    public static final String APPLICATION_HEALTH_UP_VALUE = "UP.";
    public static final String EVENT_PERSISTOR_HEALTH_KEY = "event_persistor_status";
    public static final String EVENT_PERSISTOR_HEALTH_UP_VALUE = "UP.";
    public static final String EVENT_PERSISTOR_HEALTH_DOWN_VALUE = "DOWN.";

    private HealthCheckMessage() {
    }
}
