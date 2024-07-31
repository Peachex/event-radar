package by.klevitov.eventparser.util;

import by.klevitov.eventradarcommon.dto.EventDateDTO;
import by.klevitov.eventradarcommon.dto.EventPriceDTO;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static by.klevitov.eventparser.util.EventCreatorUtil.createEventDate;
import static by.klevitov.eventparser.util.EventCreatorUtil.createEventPrice;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventCreatorUtilTest {
    @ParameterizedTest
    @MethodSource("pairDateProvider")
    public void test_createEventDate(Pair<EventDateDTO, Pair<String, String>> arguments) {
        EventDateDTO expected = arguments.getKey();
        EventDateDTO actual = createEventDate(arguments.getValue().getKey(), arguments.getValue().getValue());
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("pairPriceProvider")
    public void test_createEventPrice(Pair<EventPriceDTO, Pair<String, String>> arguments) {
        EventPriceDTO expected = arguments.getKey();
        EventPriceDTO actual = createEventPrice(arguments.getValue().getKey(), arguments.getValue().getValue());
        assertEquals(expected, actual);
    }

    private static Stream<Pair<EventDateDTO, Pair<String, String>>> pairDateProvider() {
        return Stream.of(
                Pair.of(new EventDateDTO(), Pair.of(null, null)),
                Pair.of(new EventDateDTO(LocalDate.of(2024, 6, 11), null),
                        Pair.of("2024-06-11", null)),
                Pair.of(new EventDateDTO(null, LocalDate.of(2024, 6, 11)),
                        Pair.of(null, "2024-06-11")),
                Pair.of(new EventDateDTO(LocalDate.of(2024, 6, 11),
                        LocalDate.of(2024, 12, 31)), Pair.of("2024-06-11", "2024-12-31"))
        );
    }

    private static Stream<Pair<EventPriceDTO, Pair<String, String>>> pairPriceProvider() {
        return Stream.of(
                Pair.of(new EventPriceDTO(), Pair.of(null, null)),

                Pair.of(new EventPriceDTO(new BigDecimal("12.34"), null),
                        Pair.of("12.34", null)),

                Pair.of(new EventPriceDTO(null, new BigDecimal("56.78")),
                        Pair.of(null, "56.78")),

                Pair.of(new EventPriceDTO(new BigDecimal("12.34"), new BigDecimal("56.78")),
                        Pair.of("12.34", "56.78"))
        );
    }
}
