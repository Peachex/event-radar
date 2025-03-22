package by.klevitov.eventpersistor.util;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.entity.ByCardEvent;
import by.klevitov.eventpersistor.exception.EventValidatorException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static by.klevitov.eventpersistor.util.EventValidator.throwExceptionInCaseOfEmptyIds;
import static by.klevitov.eventpersistor.util.EventValidator.validateEventBeforeCreation;
import static by.klevitov.eventpersistor.util.EventValidator.validateEventBeforeUpdating;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class EventValidatorTest {
    @ParameterizedTest
    @MethodSource("eventsForCreation")
    public void test_validateEventBeforeCreation(Pair<AbstractEvent, Boolean> eventsWithExpectedValues) {
        AbstractEvent event = eventsWithExpectedValues.getKey();
        boolean eventIsValid = eventsWithExpectedValues.getValue();
        if (eventIsValid) {
            assertDoesNotThrow(() -> validateEventBeforeCreation(event));
        } else {
            assertThrowsExactly(EventValidatorException.class, () -> validateEventBeforeCreation(event));
        }
    }

    private static Stream<Pair<AbstractEvent, Boolean>> eventsForCreation() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of(new AfishaRelaxEvent(), false),
                Pair.of(AfishaRelaxEvent.builder().title("title").build(), false),
                Pair.of(ByCardEvent.builder().title("title").dateStr("dateStr").category("category")
                        .sourceType(BYCARD).build(), true)
        );
    }

    @ParameterizedTest
    @MethodSource("eventsForUpdating")
    public void test_validateEventsBeforeUpdating(Pair<AbstractEvent, Boolean> eventsWithExpectedValues) {
        AbstractEvent event = eventsWithExpectedValues.getKey();
        boolean eventIsValid = eventsWithExpectedValues.getValue();
        if (eventIsValid) {
            assertDoesNotThrow(() -> validateEventBeforeUpdating(event));
        } else {
            assertThrowsExactly(EventValidatorException.class, () -> validateEventBeforeUpdating(event));
        }
    }

    private static Stream<Pair<AbstractEvent, Boolean>> eventsForUpdating() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of(new AfishaRelaxEvent(), false),
                Pair.of(AfishaRelaxEvent.builder().title("title").build(), false),
                Pair.of(ByCardEvent.builder().id("id").build(), true)
        );
    }

    @ParameterizedTest
    @MethodSource("eventsId")
    public void test_throwExceptionInCaseOfEmptyId(Pair<String, Boolean> eventsIdWithExpectedValues) {
        String eventsId = eventsIdWithExpectedValues.getKey();
        boolean eventIdIsValid = eventsIdWithExpectedValues.getValue();
        if (eventIdIsValid) {
            assertDoesNotThrow(() -> throwExceptionInCaseOfEmptyIds(eventsId));
        } else {
            assertThrowsExactly(EventValidatorException.class, () -> throwExceptionInCaseOfEmptyIds(eventsId));
        }
    }

    private static Stream<Pair<String, Boolean>> eventsId() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of("", false),
                Pair.of("id", true)
        );
    }

    @ParameterizedTest
    @MethodSource("eventsIds")
    public void test_throwExceptionInCaseOfEmptyIds(Pair<List<String>, Boolean> eventsIdsWithExpectedValues) {
        List<String> eventsIds = eventsIdsWithExpectedValues.getKey();
        boolean eventIdIsValid = eventsIdsWithExpectedValues.getValue();
        if (eventIdIsValid) {
            assertDoesNotThrow(() -> EventValidator.throwExceptionInCaseOfEmptyIds(eventsIds));
        } else {
            assertThrowsExactly(EventValidatorException.class, () -> EventValidator.throwExceptionInCaseOfEmptyIds(eventsIds));
        }
    }

    private static Stream<Pair<List<String>, Boolean>> eventsIds() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of(emptyList(), false),
                Pair.of(List.of("i1", "id2"), true)
        );
    }
}
