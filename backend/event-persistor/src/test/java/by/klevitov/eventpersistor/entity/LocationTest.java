package by.klevitov.eventpersistor.entity;

import by.klevitov.eventpersistor.entity.Location;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {
    @Test
    public void test_createIdBasedOnCountryAndCity() {
        Location location = new Location("id", "country", "city");
        String expected = "country:city";
        String actual = location.createIdBasedOnCountryAndCity();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void test_copyValuesForNullOrEmptyFieldsFromLocation(Pair<Location, Pair<Location, Location>> doublePair) {
        Location existentLocation = doublePair.getKey();
        Location actual = doublePair.getValue().getValue();
        actual.copyValuesForNullOrEmptyFieldsFromLocation(existentLocation);
        Location expected = doublePair.getValue().getKey();
        assertEquals(expected, actual);
    }

    private static Stream<Pair<Location, Pair<Location, Location>>> doublePairProvider() {
        return Stream.of(
                Pair.of(new Location("id", "country", "city"),
                        Pair.of(new Location("id", "updatedCountry", "city"),
                                new Location("id", "updatedCountry", null))),
                Pair.of(new Location("id", "country", "city"),
                        Pair.of(new Location("id", "country", "updatedCity"),
                                new Location("id", null, "updatedCity"))),
                Pair.of(new Location("id", "country", "city"),
                        Pair.of(new Location("id", "updatedCountry", "updatedCity"),
                                new Location("id", "updatedCountry", "updatedCity")))
        );
    }
}
