package by.klevitov.eventpersistor.healthcheck.constant;

public final class HealthCheckMessage {
    public static final String APPLICATION_HEALTH_KEY = "application_status";
    public static final String APPLICATION_HEALTH_UP_VALUE = "UP.";
    public static final String DATABASE_HEALTH_KEY = "database_status";
    public static final String DATABASE_HEALTH_UP_VALUE = "UP.";
    public static final String DATABASE_HEALTH_DOWN_VALUE = "DOWN.";

    private HealthCheckMessage() {
    }
}
