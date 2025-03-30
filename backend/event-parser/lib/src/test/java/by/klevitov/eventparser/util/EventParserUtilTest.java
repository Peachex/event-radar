package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.DateConversionException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static by.klevitov.eventparser.util.EventParserUtil.DEFAULT_DATE_FORMATTER;
import static by.klevitov.eventparser.util.EventParserUtil.EventDateLocale.RUSSIAN;
import static by.klevitov.eventparser.util.EventParserUtil.convertStringToLocalDate;
import static by.klevitov.eventparser.util.EventParserUtil.parseDateAndAddToMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertThrows(DateConversionException.class, () -> parseDateAndAddToMap(null));
    }

    @Test
    public void test_convertStringToLocalDate_withNullDateStrAndNullFormatter() {
        assertThrows(DateConversionException.class, () -> convertStringToLocalDate(null, null));
    }

    @Test
    public void test_convertStringToLocalDate_withEmptyDateStrAndNullFormatter() {
        assertThrows(DateConversionException.class, () -> convertStringToLocalDate("", null));
    }

    @Test
    public void test_convertStringToLocalDate_withInvalidDateStrAndNullFormatter() {
        assertThrowsExactly(DateConversionException.class, () -> convertStringToLocalDate("invalidDate", null));
    }

    @Test
    public void test_convertStringToLocalDate_withValidDateStrAndNullFormatter() {
        Pair<LocalDate, LocalDate> expected = Pair.of(LocalDate.of(LocalDate.now().getYear(), 6, 11), null);
        Pair<LocalDate, LocalDate> actual = convertStringToLocalDate("с 11 июня", null);
        assertEquals(expected, actual);
    }

    @Test
    public void test_convertStringToLocalDate_withValidDateStrAndNotNullFormatter() {
        Pair<LocalDate, LocalDate> expected = Pair.of(null,
                LocalDate.of(LocalDate.now().getYear(), 6, 11));
        Pair<LocalDate, LocalDate> actual = convertStringToLocalDate("только по 11 июня",
                DateTimeFormatter.ofPattern("только по d MMMM yyyy", RUSSIAN.getLocale()));
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"11 июня", "с 11 июня", "по 11 июня", "до 11 июня"})
    public void test_dateIsSingle_withSingleDate(String singleDateStr) throws Exception {
        Method privateMethod = EventParserUtil.class.getDeclaredMethod("dateIsSingle", String.class);
        privateMethod.setAccessible(true);
        assertTrue((boolean) privateMethod.invoke(EventParserUtil.class, singleDateStr));
    }

    @ParameterizedTest
    @ValueSource(strings = {"11 июня - 15 июля", "c 11 июня - по 15 июля"})
    public void test_dateIsSingle_withDualDate(String dualDateStr) throws Exception {
        Method privateMethod = EventParserUtil.class.getDeclaredMethod("dateIsSingle", String.class);
        privateMethod.setAccessible(true);
        assertFalse((boolean) privateMethod.invoke(EventParserUtil.class, dualDateStr));
    }

    @ParameterizedTest
    @MethodSource("datePairProvider")
    public void test_parseLocalDate_withValidDateWithDefaultFormatter(Pair<LocalDate, String> arguments) throws Exception {
        Method privateMethod = EventParserUtil.class.getDeclaredMethod("parseLocalDate", String.class,
                DateTimeFormatter.class);
        privateMethod.setAccessible(true);
        LocalDate expected = arguments.getKey();
        LocalDate actual = (LocalDate) privateMethod.invoke(EventParserUtil.class, arguments.getValue(),
                DEFAULT_DATE_FORMATTER);
        assertEquals(expected, actual);
    }

    @Test
    public void test_parseLocalDate_withValidDateWithCustomFormatter() throws Exception {
        Method privateMethod = EventParserUtil.class.getDeclaredMethod("parseLocalDate", String.class,
                DateTimeFormatter.class);
        privateMethod.setAccessible(true);
        LocalDate expected = LocalDate.of(LocalDate.now().getYear(), 12, 5);
        LocalDate actual = (LocalDate) privateMethod.invoke(EventParserUtil.class, "только по 5 декабря",
                DateTimeFormatter.ofPattern("только по d MMMM yyyy", RUSSIAN.getLocale()));
        assertEquals(expected, actual);
    }

    private static Stream<Pair<LocalDate, String>> datePairProvider() {
        return Stream.of(
                Pair.of(LocalDate.of(LocalDate.now().getYear(), 6, 11), "11 июня"),
                Pair.of(LocalDate.of(LocalDate.now().getYear(), 8, 23), "с 23 августа"),
                Pair.of(LocalDate.of(LocalDate.now().getYear(), 10, 5), "по 5 октября"),
                Pair.of(LocalDate.of(LocalDate.now().getYear(), 5, 30), "до 30 мая")
        );
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

        Map<String, String> expectedMapWithNullStartDateAndExclusiveEndDate = new HashMap<>();
        expectedMapWithNullStartDateAndExclusiveEndDate.put("dateStr", "до 31 декабря");

        Map<String, String> mapWithNullStartDateAndExclusiveEndDate = new HashMap<>();
        mapWithNullStartDateAndExclusiveEndDate.put("dateStr", "до 31 декабря");
        mapWithNullStartDateAndExclusiveEndDate.put("startDate", null);
        mapWithNullStartDateAndExclusiveEndDate.put("endDate", LocalDate.now().getYear() + "-12-31");

        Map<String, String> expectedMapWithSingleDate = new HashMap<>();
        expectedMapWithSingleDate.put("dateStr", "31 декабря");

        Map<String, String> mapWithSingleDate = new HashMap<>();
        mapWithSingleDate.put("dateStr", "31 декабря");
        mapWithSingleDate.put("startDate", LocalDate.now().getYear() + "-12-31");
        mapWithSingleDate.put("endDate", LocalDate.now().getYear() + "-12-31");

        return Stream.of(
                Pair.of(expectedMapWithoutNullDates, mapWithoutNullDates),
                Pair.of(expectedMapWithNullEndDate, mapWithNullEndDate),
                Pair.of(expectedMapWithNullStartDate, mapWithNullStartDate),
                Pair.of(expectedMapWithNullStartDateAndExclusiveEndDate, mapWithNullStartDateAndExclusiveEndDate),
                Pair.of(expectedMapWithSingleDate, mapWithSingleDate)
        );
    }
}
