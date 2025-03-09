package by.klevitov.eventpersistor.constant;

public final class PersistorExceptionMessage {
    public static final String NULL_OR_EMPTY_LOCATION_ID = "Location id cannot be null or empty.";
    public static final String LOCATION_NOT_FOUND = "Cannot find location with id: '%s'";
    public static final String NULL_LOCATION = "Location cannot be null.";
    public static final String NULL_OR_EMPTY_LOCATION_COUNTRY = "Location country cannot be null or empty.";
    public static final String NULL_OR_EMPTY_LOCATION_CITY = "Location city cannot be null or empty.";
    public static final String NULL_OR_EMPTY_LOCATION_COUNTRY_OR_CITY = "Location country/city cannot be null or empty.";
    public static final String LOCATION_ALREADY_EXISTS = "Location with country: '%s', city: '%s' already exists. "
            + "Location id: '%s'.";
    public static final String NULL_EVENT = "Event cannot be null.";
    public static final String NULL_OR_EMPTY_EVENT_TITLE = "Event title cannot be null or empty.";
    public static final String NULL_OR_EMPTY_EVENT_DATE_STR = "Event dateStr cannot be null or empty.";
    public static final String NULL_OR_EMPTY_EVENT_CATEGORY = "Event category cannot be null or empty.";
    public static final String NULL_OR_EMPTY_EVENT_SOURCE_TYPE = "Event sourceType cannot be null or empty.";
    public static final String NULL_OR_EMPTY_EVENT_ID = "Event id cannot be null or empty.";
    public static final String EVENT_NOT_FOUND = "Cannot find event with id: '%s'";
    public static final String EVENT_ALREADY_EXISTS = "Event with title: '%s', category: '%s', source type: '%s' " +
            "already exists. Event id: '%s'.";
    public static final String LOCATION_IS_IN_USE = "Location with id: '%s' cannot be deleted because it is in use.";
    public static final String INVALID_EVENT_CLASS = "Invalid event class: %s for event %s";
    public static final String ENTITY_CONVERTER_NOT_FOUND = "No entity converter found for: %s";
    public static final String NULL_DTO = "DTO cannot be null.";
    public static final String NULL_ENTITY = "Entity cannot be null.";
    public static final String NULL_PAGE_REQUEST = "Page request cannot be null.";
    public static final String INVALID_PAGE_NUMBER = "Invalid page number: '%d'. Page number cannot be less than zero.";
    public static final String INVALID_PAGE_SIZE = "Invalid page size: '%d'. Page size cannot be less than one.";
    public static final String NULL_SORT_FIELD = "Sort field value cannot be null.";
    public static final String NULL_SORT_FIELD_DIRECTION = "Sort field direction cannot be null.";
    public static final String INVALID_SORT_DIRECTION = "Invalid Sort direction: '%s'. Sort direction can be either "
            + "ASC or DESC.";

    private PersistorExceptionMessage() {
    }
}
