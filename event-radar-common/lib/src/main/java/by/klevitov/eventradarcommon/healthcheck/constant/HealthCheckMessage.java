package by.klevitov.eventradarcommon.healthcheck.constant;

public final class HealthCheckMessage {
    public static final String APPLICATION_HEALTH_KEY = "application_status";
    public static final String DATABASE_HEALTH_KEY = "database_status";
    public static final String MESSAGE_BROKER_HEALTH_KEY = "message_broker_status";
    public static final String EVENT_PERSISTOR_HEALTH_KEY = "event_persistor_status";
    public static final String HEALTH_UP_VALUE = "UP.";
    public static final String HEALTH_DOWN_VALUE = "DOWN.";

    private HealthCheckMessage() {
    }
}
