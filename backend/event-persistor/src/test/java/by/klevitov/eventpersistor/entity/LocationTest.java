package by.klevitov.eventpersistor.entity;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {
    @Test
    public void test_createIdBasedOnRawAddressAndName() {
        Location location = new Location();
        location.setRawAddress("RawAddress");
        location.setName("Name");

        String expected = "rawaddress:name";
        String actual = location.createIdBasedOnRawAddressAndName();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void test_copyValuesForNullOrEmptyFieldsFromLocation(Pair<Location, Pair<Location, Location>> doublePair) {
        Location source = doublePair.getKey();
        Location expected = doublePair.getValue().getLeft();
        Location actual = doublePair.getValue().getRight();

        actual.copyValuesForNullOrEmptyFieldsFromLocation(source);
        assertEquals(expected, actual);
    }

    @Test
    public void test_updateRawAddressAndNameWithDefaultsIfNull() {
        Location location = new Location();
        location.setRawAddress(null);
        location.setName(null);

        location.updateRawAddressAndNameWithDefaultsIfNull();

        assertEquals("", location.getRawAddress());
        assertEquals("", location.getName());
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
