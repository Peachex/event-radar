package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.PriceConversionException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static by.klevitov.eventparser.util.ByCardEventParserUtil.DEFAULT_PRICE_CLEANING_PATTERN;
import static by.klevitov.eventparser.util.ByCardEventParserUtil.convertStringToBigDecimal;
import static by.klevitov.eventparser.util.ByCardEventParserUtil.parsePriceAndAddToMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ByCardEventParserUtilTest {
    @ParameterizedTest
    @MethodSource("pairPriceProvider")
    public void test_parsePriceAndAddToMap(Pair<Map<String, String>, Map<String, String>> arguments) {
        Map<String, String> expected = arguments.getValue();
        Map<String, String> actual = arguments.getKey();
        parsePriceAndAddToMap(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void test_parsePriceAndAddToMap_withNullMap() {
        Exception exception = assertThrows(PriceConversionException.class, () -> parsePriceAndAddToMap(null));
        String expectedMessage = "Fields map cannot be null or empty.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_convertStringToBigDecimal_withNullPriceStrAndNullPattern() {
        Exception exception = assertThrows(PriceConversionException.class, () ->
                convertStringToBigDecimal(null, null));
        String expectedMessage = "Price cannot be null or empty: null";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_convertStringToBigDecimal_withEmptyPriceStrAndNullPattern() {
        Exception exception = assertThrows(PriceConversionException.class, () ->
                convertStringToBigDecimal("", null));
        String expectedMessage = "Price cannot be null or empty: ";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_convertStringToBigDecimal_withInvalidPriceStrAndNullPattern() {
        Exception exception = assertThrows(PriceConversionException.class, () ->
                convertStringToBigDecimal("invalidPrice", null));
        String expectedMessage = "Price cannot be parsed from string: invalidPrice";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_convertStringToBigDecimal_withValidPriceStrAndNullPattern() {
        Pair<BigDecimal, BigDecimal> expected = Pair.of(new BigDecimal("10.00"), null);
        Pair<BigDecimal, BigDecimal> actual = convertStringToBigDecimal("от 10.00 руб.", null);
        assertEquals(expected, actual);
    }

    @Test
    public void test_convertStringToBigDecimal_withValidPriceStrAndNotNullPattern() {
        Pair<BigDecimal, BigDecimal> expected = Pair.of(new BigDecimal("10.00"), null);
        Pair<BigDecimal, BigDecimal> actual = convertStringToBigDecimal("от 10_00 руб.", "[^\\d_]|\\.$");
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"10.00 руб.", "от 10.00 руб.", "до 10.00 руб."})
    public void test_priceIsSingle_withSinglePrice(String singlePriceStr) throws Exception {
        Method privateMethod = ByCardEventParserUtil.class.getDeclaredMethod("priceIsSingle", String.class);
        privateMethod.setAccessible(true);
        assertTrue((boolean) privateMethod.invoke(ByCardEventParserUtil.class, singlePriceStr));
    }

    @ParameterizedTest
    @ValueSource(strings = {"10.00 - 33.00 руб.", "от 10.00 - до 33.00 руб."})
    public void test_priceIsSingle_withDualPrice(String dualPriceStr) throws Exception {
        Method privateMethod = ByCardEventParserUtil.class.getDeclaredMethod("priceIsSingle", String.class);
        privateMethod.setAccessible(true);
        assertFalse((boolean) privateMethod.invoke(ByCardEventParserUtil.class, dualPriceStr));
    }

    @ParameterizedTest
    @MethodSource("priceProvider")
    public void test_parseBigDecimalPrice_withValidPriceWithDefaultPattern(Pair<BigDecimal,
            String> arguments) throws Exception {
        Method privateMethod = ByCardEventParserUtil.class.getDeclaredMethod("parseBigDecimalPrice", String.class,
                String.class);
        privateMethod.setAccessible(true);
        BigDecimal expected = arguments.getKey();
        BigDecimal actual = (BigDecimal) privateMethod.invoke(ByCardEventParserUtil.class, arguments.getValue(),
                DEFAULT_PRICE_CLEANING_PATTERN);
        assertEquals(expected, actual);
    }

    @Test
    public void test_parseBigDecimalPrice_withValidPriceWithCustomPattern() throws Exception {
        Method privateMethod = ByCardEventParserUtil.class.getDeclaredMethod("parseBigDecimalPrice", String.class,
                String.class);
        privateMethod.setAccessible(true);
        BigDecimal expected = new BigDecimal("12.99");
        BigDecimal actual = (BigDecimal) privateMethod.invoke(ByCardEventParserUtil.class, "от 12_99 руб.",
                "[^\\d_]|\\.$");
        assertEquals(expected, actual);
    }

    private static Stream<Pair<BigDecimal, String>> priceProvider() {
        return Stream.of(
                Pair.of(new BigDecimal("10.00"), "10.00 руб."),
                Pair.of(new BigDecimal("123.45"), "от 123.45 руб."),
                Pair.of(new BigDecimal("20"), "до 20 руб.")
        );
    }

    private static Stream<Pair<Map<String, String>, Map<String, String>>> pairPriceProvider() {
        Map<String, String> expectedMapWithoutNullPrices = new HashMap<>();
        expectedMapWithoutNullPrices.put("priceStr", "от 10.00 - до 33.00 руб.");

        Map<String, String> mapWithoutNullPrices = new HashMap<>();
        mapWithoutNullPrices.put("priceStr", "от 10.00 - до 33.00 руб.");
        mapWithoutNullPrices.put("minPrice", "10.00");
        mapWithoutNullPrices.put("maxPrice", "33.00");

        Map<String, String> expectedMapWithNullMaxPrice = new HashMap<>();
        expectedMapWithNullMaxPrice.put("priceStr", "от 10.00 руб.");

        Map<String, String> mapWithNullMaxPrice = new HashMap<>();
        mapWithNullMaxPrice.put("priceStr", "от 10.00 руб.");
        mapWithNullMaxPrice.put("minPrice", "10.00");
        mapWithNullMaxPrice.put("maxPrice", null);

        Map<String, String> expectedMapWithNullMinPrice = new HashMap<>();
        expectedMapWithNullMinPrice.put("priceStr", "до 10.00 руб.");

        Map<String, String> mapWithNullMinPrice = new HashMap<>();
        mapWithNullMinPrice.put("priceStr", "до 10.00 руб.");
        mapWithNullMinPrice.put("minPrice", null);
        mapWithNullMinPrice.put("maxPrice", "10.00");


        Map<String, String> expectedMapWithSinglePrice = new HashMap<>();
        expectedMapWithSinglePrice.put("priceStr", "10.00 руб");

        Map<String, String> mapWithSinglePrice = new HashMap<>();
        mapWithSinglePrice.put("priceStr", "10.00 руб");
        mapWithSinglePrice.put("minPrice", "10.00");
        mapWithSinglePrice.put("maxPrice", "10.00");

        return Stream.of(
                Pair.of(expectedMapWithoutNullPrices, mapWithoutNullPrices),
                Pair.of(expectedMapWithNullMaxPrice, mapWithNullMaxPrice),
                Pair.of(expectedMapWithNullMinPrice, mapWithNullMinPrice),
                Pair.of(expectedMapWithSinglePrice, mapWithSinglePrice)
        );
    }
}
