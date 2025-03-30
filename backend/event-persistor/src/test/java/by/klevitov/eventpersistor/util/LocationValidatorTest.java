package by.klevitov.eventpersistor.util;

import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.exception.LocationValidatorException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static by.klevitov.eventpersistor.util.LocationValidator.throwExceptionInCaseOfEmptyId;
import static by.klevitov.eventpersistor.util.LocationValidator.validateLocationBeforeCreation;
import static by.klevitov.eventpersistor.util.LocationValidator.validateLocationBeforeUpdating;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class LocationValidatorTest {

    @ParameterizedTest
    @MethodSource("locationsForCreation")
    public void test_validateLocationBeforeCreation(Pair<Location, Boolean> locationsWithExpectedValues) {
        Location location = locationsWithExpectedValues.getKey();
        boolean locationIsValid = locationsWithExpectedValues.getValue();
        if (locationIsValid) {
            assertDoesNotThrow(() -> validateLocationBeforeCreation(location));
        } else {
            assertThrowsExactly(LocationValidatorException.class, () -> validateLocationBeforeCreation(location));
        }
    }

    private static Stream<Pair<Location, Boolean>> locationsForCreation() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of(new Location(), false),
                Pair.of(new Location("", ""), false),
                Pair.of(new Location("country", "city"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("locationsForUpdating")
    public void test_validateLocationBeforeUpdating(Pair<Location, Boolean> locationsWithExpectedValues) {
        Location location = locationsWithExpectedValues.getKey();
        boolean locationIsValid = locationsWithExpectedValues.getValue();
        if (locationIsValid) {
            assertDoesNotThrow(() -> validateLocationBeforeUpdating(location));
        } else {
            assertThrowsExactly(LocationValidatorException.class, () -> validateLocationBeforeUpdating(location));
        }
    }

    private static Stream<Pair<Location, Boolean>> locationsForUpdating() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of(new Location(), false),
                Pair.of(new Location("country", "city"), false),
                Pair.of(new Location("id", "", ""), false),
                Pair.of(new Location("id", "country", ""), true),
                Pair.of(new Location("id", "country", "city"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("locationsId")
    public void test_throwExceptionInCaseOfEmptyId(Pair<String, Boolean> locationsIdWithExpectedValues) {
        String locationId = locationsIdWithExpectedValues.getKey();
        boolean locationIdIsValid = locationsIdWithExpectedValues.getValue();
        if (locationIdIsValid) {
            assertDoesNotThrow(() -> throwExceptionInCaseOfEmptyId(locationId));
        } else {
            assertThrowsExactly(LocationValidatorException.class, () -> throwExceptionInCaseOfEmptyId(locationId));
        }
    }

    private static Stream<Pair<String, Boolean>> locationsId() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of("", false),
                Pair.of("id", true)
        );
    }
}
