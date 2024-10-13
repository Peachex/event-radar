package by.klevitov.eventpersistor.persistor.util;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.persistor.entity.ByCardEvent;
import by.klevitov.eventpersistor.persistor.exception.EventValidatorException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static by.klevitov.eventpersistor.persistor.util.EventValidator.throwExceptionInCaseOfEmptyId;
import static by.klevitov.eventpersistor.persistor.util.EventValidator.validateEventBeforeCreation;
import static by.klevitov.eventpersistor.persistor.util.EventValidator.validateEventBeforeUpdating;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;
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
                Pair.of(ByCardEvent.builder().title("title").dateStr("dateStr").sourceType(BYCARD).build(), true)
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
            assertDoesNotThrow(() -> throwExceptionInCaseOfEmptyId(eventsId));
        } else {
            assertThrowsExactly(EventValidatorException.class, () -> throwExceptionInCaseOfEmptyId(eventsId));
        }
    }

    private static Stream<Pair<String, Boolean>> eventsId() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of("", false),
                Pair.of("id", true)
        );
    }
}
