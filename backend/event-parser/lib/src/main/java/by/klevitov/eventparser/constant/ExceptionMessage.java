package by.klevitov.eventparser.constant;

public final class ExceptionMessage {
    public static final String NULL_OR_EMPTY_URL = "URL cannot be null or empty: %s";

    public static final String ERROR_DURING_HTML_DOCUMENT_RETRIEVING = "There was an error during html document"
            + " retrieving. More details: %s";

    public static final String ERROR_RETRIEVING_EVENTS_DTO = "There was an error during events dto retrieving."
            + " More details: %s";

    public static final String NULL_PARSER = "Parser cannot be null.";
    public static final String UNKNOWN_PARSER = "Unknown parser: %s";

    public static final String PROPERTIES_FILE_NOT_FOUND = "Property file not found. File name: %s";

    public static final String ERROR_READING_PROPERTIES_FROM_FILE = "There was an error during reading properties"
            + " from file. More details: %s";

    public static final String NULL_OR_EMPTY_PROPERTIES_FILE_NAME = "Properties file name cannot be null or empty: %s";

    public static final String NULL_OR_EMPTY_PROPERTY_KEY = "Property key cannot be null or empty: %s";

    public static final String NULL_OR_EMPTY_DATE = "Date cannot be null or empty: %s";

    public static final String NULL_OR_EMPTY_FIELDS_MAP = "Fields map cannot be null or empty.";

    public static final String ERROR_DURING_DATE_CONVERSION = "Date cannot be parsed from string: %s";

    public static final String INVALID_ARRAY_DATES_SIZE = "Array should contain only two dates, "
            + "actual array size: %s";

    public static final String NULL_DATE_DUE_TO_ERROR_DURING_CONVERSION = "There was an error during date "
            + "conversion, so the event will have null date: %s";

    public static final String NULL_OR_EMPTY_PRICE = "Price cannot be null or empty: %s";

    public static final String NULL_PRICE_DUE_TO_ERROR_DURING_CONVERSION = "There was an error during price "
            + "conversion, so the event will have null price: %s";

    public static final String INVALID_ARRAY_PRICES_SIZE = "Array should contain only two prices, "
            + "actual array size: %s";

    public static final String ERROR_DURING_PRICE_CONVERSION = "Price cannot be parsed from string: %s";

    private ExceptionMessage() {
    }
}
