package by.klevitov.eventparser.util;

import by.klevitov.eventradarcommon.dto.EventDate;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static by.klevitov.eventparser.util.EventCreatorUtil.createEventDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventCreatorUtilTest {
    @ParameterizedTest
    @MethodSource("pairProvider")
    public void test_createEventDate(Pair<EventDate, Pair<String, String>> arguments) {
        EventDate expected = arguments.getKey();
        EventDate actual = createEventDate(arguments.getValue().getKey(), arguments.getValue().getValue());
        assertEquals(expected, actual);
    }

    private static Stream<Pair<EventDate, Pair<String, String>>> pairProvider() {
        return Stream.of(
                Pair.of(new EventDate(), Pair.of(null, null)),
                Pair.of(new EventDate(LocalDate.of(2024, 6, 11), null),
                        Pair.of("2024-06-11", null)),
                Pair.of(new EventDate(null, LocalDate.of(2024, 6, 11)),
                        Pair.of(null, "2024-06-11")),
                Pair.of(new EventDate(LocalDate.of(2024, 6, 11),
                        LocalDate.of(2024, 12, 31)), Pair.of("2024-06-11", "2024-12-31"))
        );
    }
}
