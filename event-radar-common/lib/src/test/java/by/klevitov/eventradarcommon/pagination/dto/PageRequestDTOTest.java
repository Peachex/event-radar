package by.klevitov.eventradarcommon.pagination.dto;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageRequestDTOTest {
    private static final int VALID_PAGE_NUMBER = 0;
    private static final int VALID_PAGE_SIZE = 1;

    @Test
    public void test_createPageRequest_withNullSortFields() {
        PageRequestDTO pageRequestWithNullSortFields = new PageRequestDTO(VALID_PAGE_NUMBER, VALID_PAGE_SIZE, null);
        PageRequest expected = PageRequest.of(VALID_PAGE_NUMBER, VALID_PAGE_SIZE);
        PageRequest actual = pageRequestWithNullSortFields.createPageRequest();
        assertEquals(expected, actual);
    }

    @Test
    public void test_createPageRequest_withNotNullSortFields() {
        List<PageRequestDTO.SortField> sortFields = List.of(new PageRequestDTO.SortField("field", "asc"));
        PageRequestDTO pageRequestWithNotNullSortFields = new PageRequestDTO(VALID_PAGE_NUMBER, VALID_PAGE_SIZE, sortFields);
        PageRequest expected = PageRequest.of(
                VALID_PAGE_NUMBER,
                VALID_PAGE_SIZE,
                Sort.by(
                        sortFields.stream()
                                .map(s -> new Sort.Order(Sort.Direction.fromString(s.getDirection()), s.getField()))
                                .collect(Collectors.toList())
                ));
        PageRequest actual = pageRequestWithNotNullSortFields.createPageRequest();
        assertEquals(expected, actual);
    }
}
