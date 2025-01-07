package by.klevitov.eventpersistor.persistor.entity;

import by.klevitov.eventpersistor.persistor.exception.InvalidEventClassException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AfishaRelaxEventTest {
    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void test_copyValuesForNullOrEmptyFieldsFromEvent(Pair<AfishaRelaxEvent,
            Pair<AfishaRelaxEvent, AfishaRelaxEvent>> doublePair) {
        AfishaRelaxEvent existentEvent = doublePair.getKey();
        AfishaRelaxEvent actual = doublePair.getValue().getValue();
        actual.copyValuesForNullOrEmptyFieldsFromEvent(existentEvent);
        AfishaRelaxEvent expected = doublePair.getValue().getKey();
        assertEquals(expected, actual);
    }

    @Test
    public void test_copyValuesForNullOrEmptyFieldsFromEvent_withInvalidEventClass() {
        ByCardEvent existentEventOfInvalidClass = new ByCardEvent();
        AfishaRelaxEvent actual = new AfishaRelaxEvent();
        assertThrows(InvalidEventClassException.class, () ->
                actual.copyValuesForNullOrEmptyFieldsFromEvent(existentEventOfInvalidClass));
    }

    private static Stream<Pair<AfishaRelaxEvent, Pair<AfishaRelaxEvent, AfishaRelaxEvent>>> doublePairProvider() {
        return Stream.of(
                Pair.of(AfishaRelaxEvent.builder().title("title").eventLink("eventLink").build(),
                        Pair.of(AfishaRelaxEvent.builder().title("updatedTitle").eventLink("eventLink").build(),
                                AfishaRelaxEvent.builder().title("updatedTitle").build())),

                Pair.of(AfishaRelaxEvent.builder().title("title").location(
                                new Location("country", "city")).build(),
                        Pair.of(AfishaRelaxEvent.builder().title("title").location(
                                        new Location("updatedCountry", "updatedCity")).build(),
                                AfishaRelaxEvent.builder().location(
                                        new Location("updatedCountry", "updatedCity")).build()))
        );
    }
}
