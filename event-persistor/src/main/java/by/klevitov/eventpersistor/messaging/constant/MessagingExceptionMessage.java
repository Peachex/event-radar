package by.klevitov.eventpersistor.messaging.constant;

public final class MessagingExceptionMessage {
    public static final String INVALID_ENTITY_LOCATIONS_DATA = "Invalid data for creating locations: %s";
    public static final String INVALID_ENTITY_EVENTS_DATA = "Invalid data for creating events: %s";
    public static final String UNSUPPORTED_REQUEST_OR_ENTITY_TYPE = "Unsupported request or entity type: %s | %s";

    private MessagingExceptionMessage() {
    }
}
