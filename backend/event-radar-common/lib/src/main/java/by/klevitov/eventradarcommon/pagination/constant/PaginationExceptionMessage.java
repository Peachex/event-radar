package by.klevitov.eventradarcommon.pagination.constant;

public final class PaginationExceptionMessage {
    public static final String NULL_PAGE_REQUEST = "Page request cannot be null.";
    public static final String INVALID_PAGE_NUMBER = "Invalid page number: '%d'. Page number cannot be less than zero.";
    public static final String INVALID_PAGE_SIZE = "Invalid page size: '%d'. Page size cannot be less than one.";
    public static final String NULL_SORT_FIELD = "Sort field value cannot be null.";
    public static final String NULL_SORT_FIELD_DIRECTION = "Sort field direction cannot be null.";
    public static final String INVALID_SORT_DIRECTION = "Invalid Sort direction: '%s'. Sort direction can be either "
            + "ASC or DESC.";

    private PaginationExceptionMessage() {
    }
}
