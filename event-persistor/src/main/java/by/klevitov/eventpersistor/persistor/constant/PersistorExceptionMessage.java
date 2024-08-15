package by.klevitov.eventpersistor.persistor.constant;

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

    public static final String NULL_OR_EMPTY_EVENT_SOURCE_TYPE = "Event sourceType cannot be null or empty.";

    public static final String NULL_OR_EMPTY_EVENT_ID = "Event id cannot be null or empty.";

    public static final String EVENT_NOT_FOUND = "Cannot find event with id: '%s'";

    private PersistorExceptionMessage() {
    }
}
