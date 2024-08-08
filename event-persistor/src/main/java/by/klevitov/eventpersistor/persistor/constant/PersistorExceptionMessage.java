package by.klevitov.eventpersistor.persistor.constant;

public final class PersistorExceptionMessage {
    public static final String NULL_OR_EMPTY_LOCATION_ID = "Location id cannot be null or empty.";

    public static final String LOCATION_NOT_FOUND = "Cannot find location with id: %s";

    public static final String NULL_LOCATION = "Location cannot be null.";

    public static final String NULL_OR_EMPTY_LOCATION_COUNTRY = "Location country cannot be null or empty.";

    public static final String NULL_OR_EMPTY_LOCATION_CITY = "Location city cannot be null or empty.";

    private PersistorExceptionMessage() {
    }
}
