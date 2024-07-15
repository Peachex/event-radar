package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.DateConversionException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static by.klevitov.eventparser.util.EventParserUtil.EventDateLocale.RUSSIAN;
import static by.klevitov.eventparser.util.EventParserUtil.convertDateToLocalDate;
import static by.klevitov.eventparser.util.EventParserUtil.parseDateAndAddToMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EventParserUtilTest {
    @ParameterizedTest
    @MethodSource("pairProvider")
    public void test_parseDateAndAddToMap(Pair<Map<String, String>, Map<String, String>> arguments) {
        Map<String, String> expected = arguments.getValue();
        Map<String, String> actual = arguments.getKey();
        parseDateAndAddToMap(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void test_parseDateAndAddToMap_withNullMap() {
        Exception exception = assertThrows(DateConversionException.class, () -> parseDateAndAddToMap(null));
        String expectedMessage = "Fields map cannot be null or empty.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_convertDateToLocalDate_withNullDateStrAndNullFormatter() {
        Exception exception = assertThrows(DateConversionException.class, () -> convertDateToLocalDate(null, null));
        String expectedMessage = "Date cannot be null or empty: null";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_convertDateToLocalDate_withEmptyDateStrAndNullFormatter() {
        Exception exception = assertThrows(DateConversionException.class, () -> convertDateToLocalDate("", null));
        String expectedMessage = "Date cannot be null or empty: ";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_convertDateToLocalDate_withInvalidDateStrAndNullFormatter() {
        Exception exception = assertThrows(DateConversionException.class, () -> convertDateToLocalDate("invalidDate",
                null));
        String expectedMessage = "Date cannot be parsed from string: invalidDate 2024";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_convertDateToLocalDate_withValidDateStrAndNullFormatter() {
        Pair<LocalDate, LocalDate> expected = Pair.of(LocalDate.of(LocalDate.now().getYear(), 6, 11), null);
        Pair<LocalDate, LocalDate> actual = convertDateToLocalDate("с 11 июня", null);
        assertEquals(expected, actual);
    }

    @Test
    public void test_convertDateToLocalDate_withValidDateStrAndValidFormatter() {
        Pair<LocalDate, LocalDate> expected = Pair.of(null,
                LocalDate.of(LocalDate.now().getYear(), 6, 11));
        Pair<LocalDate, LocalDate> actual = convertDateToLocalDate("только по 11 июня",
                DateTimeFormatter.ofPattern("только по d MMMM yyyy", RUSSIAN.getLocale()));
        assertEquals(expected, actual);
    }

    private static Stream<Pair<Map<String, String>, Map<String, String>>> pairProvider() {
        Map<String, String> expectedMapWithoutNullDates = new HashMap<>();
        expectedMapWithoutNullDates.put("dateStr", "11 июня - 31 декабря");

        Map<String, String> mapWithoutNullDates = new HashMap<>();
        mapWithoutNullDates.put("dateStr", "11 июня - 31 декабря");
        mapWithoutNullDates.put("startDate", LocalDate.now().getYear() + "-06-11");
        mapWithoutNullDates.put("endDate", LocalDate.now().getYear() + "-12-31");

        Map<String, String> expectedMapWithNullEndDate = new HashMap<>();
        expectedMapWithNullEndDate.put("dateStr", "с 11 июня");

        Map<String, String> mapWithNullEndDate = new HashMap<>();
        mapWithNullEndDate.put("dateStr", "с 11 июня");
        mapWithNullEndDate.put("startDate", LocalDate.now().getYear() + "-06-11");
        mapWithNullEndDate.put("endDate", null);

        Map<String, String> expectedMapWithNullStartDate = new HashMap<>();
        expectedMapWithNullStartDate.put("dateStr", "по 31 декабря");

        Map<String, String> mapWithNullStartDate = new HashMap<>();
        mapWithNullStartDate.put("dateStr", "по 31 декабря");
        mapWithNullStartDate.put("startDate", null);
        mapWithNullStartDate.put("endDate", LocalDate.now().getYear() + "-12-31");

        return Stream.of(
                Pair.of(expectedMapWithoutNullDates, mapWithoutNullDates),
                Pair.of(expectedMapWithNullEndDate, mapWithNullEndDate),
                Pair.of(expectedMapWithNullStartDate, mapWithNullStartDate)
        );
    }
}
