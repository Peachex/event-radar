package by.klevitov.eventradarcommon.pagination.util;

import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.eventradarcommon.pagination.exception.PageRequestValidatorException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static by.klevitov.eventradarcommon.pagination.util.PageRequestValidator.validatePageRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class PageRequestValidatorTest {
    @ParameterizedTest
    @MethodSource("pageRequests")
    public void test_validatePageRequest(Pair<PageRequestDTO, Boolean> pageRequestsWithExpectedValue) {
        PageRequestDTO pageRequestDTO = pageRequestsWithExpectedValue.getKey();
        boolean pageRequestIsValid = pageRequestsWithExpectedValue.getValue();
        if (pageRequestIsValid) {
            assertDoesNotThrow(() -> validatePageRequest(pageRequestDTO));
        } else {
            assertThrowsExactly(PageRequestValidatorException.class, () -> validatePageRequest(pageRequestDTO));
        }
    }

    private static Stream<Pair<PageRequestDTO, Boolean>> pageRequests() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of(new PageRequestDTO(), false),
                Pair.of(new PageRequestDTO(-1, 1, null), false),
                Pair.of(new PageRequestDTO(0, -1, null), false),
                Pair.of(new PageRequestDTO(-1, -1, null), false),
                Pair.of(new PageRequestDTO(0, 1, List.of(new PageRequestDTO.SortField())), false),
                Pair.of(new PageRequestDTO(0, 1, List.of(new PageRequestDTO.SortField(null, null))), false),
                Pair.of(new PageRequestDTO(0, 1, List.of(new PageRequestDTO.SortField("", "asc"))), false),
                Pair.of(new PageRequestDTO(0, 1, List.of(new PageRequestDTO.SortField("field", ""))), false),
                Pair.of(new PageRequestDTO(0, 1, List.of(new PageRequestDTO.SortField("field", "11"))), false),
                Pair.of(new PageRequestDTO(0, 1, null), true),
                Pair.of(new PageRequestDTO(0, 1, new ArrayList<>()), true),
                Pair.of(new PageRequestDTO(0, 1, List.of(new PageRequestDTO.SortField("field", "asc"))), true),
                Pair.of(new PageRequestDTO(0, 1, List.of(new PageRequestDTO.SortField("field", "desc"))), true),
                Pair.of(new PageRequestDTO(0, 1, List.of(new PageRequestDTO.SortField("field", "AsC"))), true)
        );
    }
}
