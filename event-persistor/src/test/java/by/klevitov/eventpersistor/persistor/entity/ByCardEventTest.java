package by.klevitov.eventpersistor.persistor.entity;

import by.klevitov.eventpersistor.persistor.exception.InvalidEventClassException;
import by.klevitov.eventradarcommon.dto.EventPrice;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ByCardEventTest {
    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void test_copyValuesForNullOrEmptyFieldsFromEvent(Pair<ByCardEvent,
            Pair<ByCardEvent, ByCardEvent>> doublePair) {
        ByCardEvent existentEvent = doublePair.getKey();
        ByCardEvent actual = doublePair.getValue().getValue();
        actual.copyValuesForNullOrEmptyFieldsFromEvent(existentEvent);
        ByCardEvent expected = doublePair.getValue().getKey();
        assertEquals(expected, actual);
    }

    @Test
    public void test_copyValuesForNullOrEmptyFieldsFromEvent_withInvalidEventClass() {
        AfishaRelaxEvent existentEventOfInvalidClass = new AfishaRelaxEvent();
        ByCardEvent actual = new ByCardEvent();
        assertThrows(InvalidEventClassException.class, () ->
                actual.copyValuesForNullOrEmptyFieldsFromEvent(existentEventOfInvalidClass));
    }

    private static Stream<Pair<ByCardEvent, Pair<ByCardEvent, ByCardEvent>>> doublePairProvider() {
        return Stream.of(
                Pair.of(ByCardEvent.builder().title("title").eventLink("eventLink").build(),
                        Pair.of(ByCardEvent.builder().title("updatedTitle").eventLink("eventLink").build(),
                                ByCardEvent.builder().title("updatedTitle").build())),

                Pair.of(ByCardEvent.builder().title("title").location(
                                new Location("country", "city")).build(),
                        Pair.of(ByCardEvent.builder().title("title").location(
                                        new Location("updatedCountry", "updatedCity")).build(),
                                ByCardEvent.builder().location(
                                        new Location("updatedCountry", "updatedCity")).build())),

                Pair.of(ByCardEvent.builder().title("title").price(
                                new EventPrice(new BigDecimal(1), new BigDecimal(2))).build(),
                        Pair.of(ByCardEvent.builder().title("title").price(
                                        new EventPrice(new BigDecimal(3), new BigDecimal(4))).build(),
                                ByCardEvent.builder().price(
                                        new EventPrice(new BigDecimal(3), new BigDecimal(4))).build()))
        );
    }
}
