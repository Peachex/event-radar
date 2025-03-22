package by.klevitov.eventradarcommon.pagination.util;

import java.util.List;

public final class PaginationUtil {
    private PaginationUtil() {
    }

    public static void trimToPageSize(final List<?> elements, final int pageSize) {
        while (elements.size() > pageSize) {
            elements.remove(elements.size() - 1);
        }
    }
}
