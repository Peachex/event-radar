package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.PriceConversionException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.Map;

import static by.klevitov.eventparser.constant.EventField.MAX_PRICE;
import static by.klevitov.eventparser.constant.EventField.MIN_PRICE;
import static by.klevitov.eventparser.constant.EventField.PRICE_STR;
import static by.klevitov.eventparser.constant.ExceptionMessage.ERROR_DURING_PRICE_CONVERSION;
import static by.klevitov.eventparser.constant.ExceptionMessage.INVALID_ARRAY_PRICES_SIZE;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_FIELDS_MAP;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_PRICE;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_PRICE_DUE_TO_ERROR_DURING_CONVERSION;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
public final class ByCardEventParserUtil {
    public static final String DEFAULT_PRICE_CLEANING_PATTERN = "[^\\d.,]|\\.$|,$";
    private static final String PRICE_SPLIT_REGEX = "-";
    private static final String START_PRICE_PREFIX = "от";
    private static final int EXPECTED_ARRAY_PRICES_SIZE = 2;
    private static final String PRICE_DELIMITER = ".";
    private static final String NON_DIGIT_PATTERN = "\\D";

    private ByCardEventParserUtil() {
    }

    public static void parsePriceAndAddToMap(final Map<String, String> fields) {
        throwExceptionInCaseOfEmptyMap(fields);
        Pair<BigDecimal, BigDecimal> prices = Pair.of(null, null);
        try {
            prices = convertStringToBigDecimal(fields.get(PRICE_STR), null);
        } catch (PriceConversionException e) {
            log.warn(String.format(NULL_PRICE_DUE_TO_ERROR_DURING_CONVERSION, e));
        } finally {
            fields.put(MIN_PRICE, prices.getLeft() != null ? prices.getLeft().toString() : null);
            fields.put(MAX_PRICE, prices.getRight() != null ? prices.getRight().toString() : null);
        }
    }

    private static void throwExceptionInCaseOfEmptyMap(final Map<String, String> fields) {
        if (fields == null) {
            log.error(NULL_OR_EMPTY_FIELDS_MAP);
            throw new PriceConversionException(NULL_OR_EMPTY_FIELDS_MAP);
        }
    }

    public static Pair<BigDecimal, BigDecimal> convertStringToBigDecimal(final String price, final String pattern) {
        throwExceptionInCaseOfEmptyPrice(price);
        final String patterToUse = definePatternToUse(pattern);
        return convertPrice(price, patterToUse);
    }

    private static void throwExceptionInCaseOfEmptyPrice(final String price) {
        if (isEmpty(price)) {
            final String exceptionMessage = String.format(NULL_OR_EMPTY_PRICE, price);
            log.error(exceptionMessage);
            throw new PriceConversionException(exceptionMessage);
        }
    }

    private static String definePatternToUse(final String pattern) {
        return (pattern != null ? pattern : DEFAULT_PRICE_CLEANING_PATTERN);
    }

    private static Pair<BigDecimal, BigDecimal> convertPrice(final String priceStr, final String pattern) {
        return (priceIsSingle(priceStr)
                ? convertSinglePrice(priceStr, pattern)
                : convertDualPrice(priceStr, pattern));
    }

    private static boolean priceIsSingle(final String price) {
        return (price.split(PRICE_SPLIT_REGEX).length == 1);
    }

    private static Pair<BigDecimal, BigDecimal> convertSinglePrice(final String priceStr, final String pattern) {
        BigDecimal price = parseBigDecimalPrice(priceStr, pattern);
        if (Character.isDigit(priceStr.toLowerCase().charAt(0))) {
            return Pair.of(price, price);
        }
        return (priceStr.toLowerCase().startsWith(START_PRICE_PREFIX)
                ? Pair.of(price, null)
                : Pair.of(null, price));
    }

    private static Pair<BigDecimal, BigDecimal> convertDualPrice(final String priceStr, final String pattern) {
        String[] prices = priceStr.split(PRICE_SPLIT_REGEX);
        throwExceptionInCaseOfInvalidSizeOfArrayWithPrices(prices);
        final String minPriceStr = prices[0].trim();
        final String maxPriceStr = prices[1].trim();
        return Pair.of(parseBigDecimalPrice(minPriceStr, pattern), parseBigDecimalPrice(maxPriceStr, pattern));
    }

    private static void throwExceptionInCaseOfInvalidSizeOfArrayWithPrices(final String[] prices) {
        if (prices.length != EXPECTED_ARRAY_PRICES_SIZE) {
            final String exceptionMessage = String.format(INVALID_ARRAY_PRICES_SIZE, prices.length);
            log.error(exceptionMessage);
            throw new PriceConversionException(exceptionMessage);
        }
    }

    private static BigDecimal parseBigDecimalPrice(final String priceStr, final String pattern) {
        try {
            String cleanPrice = priceStr
                    .replaceAll(pattern, StringUtils.EMPTY)
                    .replaceAll(NON_DIGIT_PATTERN, PRICE_DELIMITER);
            return new BigDecimal(cleanPrice);
        } catch (Exception e) {
            throwExceptionInCaseOfPriceCannotBeConvert(priceStr, e);
        }
        return null;
    }

    private static void throwExceptionInCaseOfPriceCannotBeConvert(final String price, final Exception e) {
        final String exceptionMessage = String.format(ERROR_DURING_PRICE_CONVERSION, price);
        log.error(exceptionMessage);
        throw new PriceConversionException(exceptionMessage, e);
    }
}
