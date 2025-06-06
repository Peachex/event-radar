package by.klevitov.eventradarcommon.pagination.util;

import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public final class PaginationUtil {
    private PaginationUtil() {
    }

    public static void trimToPageSize(final List<?> elements, final int pageSize) {
        while (elements.size() > pageSize) {
            elements.remove(elements.size() - 1);
        }
    }

    public static void trimToPageSize(final List<?> elements, final int pageSize, final int pageNumber) {
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, elements.size());
        clearIfStartIndexOutOfBounds(elements, fromIndex);
        retainOnlyRange(elements, fromIndex, toIndex);
    }

    private static void clearIfStartIndexOutOfBounds(final List<?> elements, final int startIndex) {
        if (elements == null || elements.isEmpty()) {
            return;
        }
        if (startIndex >= elements.size()) {
            elements.clear();
        }
    }

    private static void retainOnlyRange(final List<?> elements, final int fromIndex, final int toIndex) {
        if (elements == null || elements.isEmpty()) {
            return;
        }
        if (toIndex < elements.size()) {
            elements.subList(toIndex, elements.size()).clear();
        }
        if (fromIndex > 0 && fromIndex <= elements.size()) {
            elements.subList(0, fromIndex).clear();
        }
    }

    public static void removeNonExistentSortFields(final List<PageRequestDTO.SortField> sortFields, final Class<?> clazz) {
        if (isNotEmpty(sortFields)) {
            List<Field> clazzFields = Arrays.stream(clazz.getDeclaredFields()).toList();
            List<String> clazzFieldNames = clazzFields.stream().map(Field::getName).toList();
            sortFields.removeIf(s -> !clazzFieldNames.contains(s.getField()));
        }
    }
}
