package by.klevitov.eventpersistor.messaging.constant;

public final class MessagingExceptionMessage {
    public static final String INVALID_ENTITY_LOCATION_DATA = "Invalid location data: %s";
    public static final String INVALID_ENTITY_LOCATIONS_DATA = "Invalid locations data: %s";
    public static final String INVALID_ENTITY_EVENT_DATA = "Invalid event data: %s";
    public static final String INVALID_ENTITY_EVENTS_DATA = "Invalid events data: %s";
    public static final String INVALID_SEARCH_BY_FIELDS_DATA = "Invalid search by fields data: %s";
    public static final String UNSUPPORTED_REQUEST_OR_ENTITY_TYPE = "Unsupported request or entity type: %s | %s";
    public static final String ENTITY_CONVERTER_NOT_FOUND = "No entity converter found for: %s";
    public static final String REQUEST_HANDLER_NOT_FOUND = "No request handler found for entity type '%s' and "
            + "request type '%s'.";
    public static final String NULL_DTO = "DTO cannot be null.";
    public static final String NULL_ENTITY = "Entity cannot be null.";

    private MessagingExceptionMessage() {
    }
}
