package by.klevitov.eventparser.constant;

public final class ExceptionMessages {
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

    private ExceptionMessages() {
    }
}
