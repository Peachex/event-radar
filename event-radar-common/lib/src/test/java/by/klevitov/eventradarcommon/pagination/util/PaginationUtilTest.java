package by.klevitov.eventradarcommon.pagination.util;

import by.klevitov.eventradarcommon.dto.LocationDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaginationUtilTest {
    @Test
    public void test_trimToPageSize() {
        List<String> elements = createListWithElements(4);
        List<String> expected = List.of(elements.get(0), elements.get(1));
        List<String> actual = elements;
        PaginationUtil.trimToPageSize(actual, 2);
        assertEquals(expected, actual);
    }

    private List<String> createListWithElements(int size) {
        List<String> elements = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            elements.add("element" + i);
        }
        return elements;
    }

    @Test
    public void test_removeNonExistentSortFields_withExistentFields() {
        List<PageRequestDTO.SortField> expected = List.of(new PageRequestDTO.SortField("city", "asc"));
        List<PageRequestDTO.SortField> actual = new ArrayList<>();
        actual.add(new PageRequestDTO.SortField("city", "asc"));
        PaginationUtil.removeNonExistentSortFields(actual, LocationDTO.class);
        assertEquals(expected, actual);
    }

    @Test
    public void test_removeNonExistentSortFields_withNonExistentFields() {
        List<PageRequestDTO.SortField> expected = new ArrayList<>();
        List<PageRequestDTO.SortField> actual = new ArrayList<>();
        actual.add(new PageRequestDTO.SortField("field", "asc"));
        PaginationUtil.removeNonExistentSortFields(actual, LocationDTO.class);
        assertEquals(expected, actual);
    }
}
