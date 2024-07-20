package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.DateConversionException;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Map;

import static by.klevitov.eventparser.constant.EventField.DATE_STR;
import static by.klevitov.eventparser.constant.EventField.END_DATE;
import static by.klevitov.eventparser.constant.EventField.START_DATE;
import static by.klevitov.eventparser.constant.ExceptionMessage.ERROR_DURING_DATE_CONVERSION;
import static by.klevitov.eventparser.constant.ExceptionMessage.INVALID_ARRAY_DATES_SIZE;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_DATE_DUE_TO_ERROR_DURING_CONVERSION;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_DATE;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_FIELDS_MAP;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
public final class EventParserUtil {

    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("[до d MMMM yyyy]"))
            .append(DateTimeFormatter.ofPattern("[по d MMMM yyyy]"))
            .append(DateTimeFormatter.ofPattern("[с d MMMM yyyy]"))
            .append(DateTimeFormatter.ofPattern("[d MMMM yyyy]"))
            .toFormatter();
    private static final String DATE_SPLIT_REGEX = "-";
    private static final String START_DATE_PREFIX = "с";
    private static final int EXPECTED_ARRAY_DATES_SIZE = 2;

    private EventParserUtil() {
    }

    public static void parseDateAndAddToMap(final Map<String, String> fields) {
        throwExceptionInCaseOfEmptyMap(fields);
        Pair<LocalDate, LocalDate> dates = Pair.of(null, null);
        try {
            dates = convertStringToLocalDate(fields.get(DATE_STR), null);
        } catch (DateConversionException e) {
            log.warn(String.format(NULL_DATE_DUE_TO_ERROR_DURING_CONVERSION, e));
        } finally {
            fields.put(START_DATE, dates.getLeft() != null ? dates.getLeft().toString() : null);
            fields.put(END_DATE, dates.getRight() != null ? dates.getRight().toString() : null);
        }
    }

    private static void throwExceptionInCaseOfEmptyMap(final Map<String, String> fields) {
        if (fields == null) {
            log.error(NULL_OR_EMPTY_FIELDS_MAP);
            throw new DateConversionException(NULL_OR_EMPTY_FIELDS_MAP);
        }
    }

    public static Pair<LocalDate, LocalDate> convertStringToLocalDate(final String dateStr,
                                                                      final DateTimeFormatter customFormatter) {
        throwExceptionInCaseOfEmptyDate(dateStr);
        final DateTimeFormatter formatterToUse = defineFormatterToUse(customFormatter);
        return convertDate(dateStr, formatterToUse);
    }

    private static void throwExceptionInCaseOfEmptyDate(final String date) {
        if (isEmpty(date)) {
            final String exceptionMessage = String.format(NULL_OR_EMPTY_DATE, date);
            log.error(exceptionMessage);
            throw new DateConversionException(exceptionMessage);
        }
    }

    private static DateTimeFormatter defineFormatterToUse(final DateTimeFormatter customFormatter) {
        return (customFormatter != null ? customFormatter : DEFAULT_DATE_FORMATTER);
    }

    private static Pair<LocalDate, LocalDate> convertDate(final String dateStr, final DateTimeFormatter formatter) {
        return (dateIsSingle(dateStr) ? convertSingleDate(dateStr, formatter) : convertDualDate(dateStr, formatter));
    }

    private static boolean dateIsSingle(final String date) {
        return (date.split(DATE_SPLIT_REGEX).length == 1);
    }

    private static Pair<LocalDate, LocalDate> convertSingleDate(final String dateStr,
                                                                final DateTimeFormatter formatter) {
        LocalDate localDate = parseLocalDate(dateStr, formatter);
        if (Character.isDigit(dateStr.toLowerCase().charAt(0))) {
            return Pair.of(localDate, localDate);
        }
        return (dateStr.toLowerCase().startsWith(START_DATE_PREFIX)
                ? Pair.of(localDate, null)
                : Pair.of(null, localDate));
    }

    private static Pair<LocalDate, LocalDate> convertDualDate(final String dateStr, final DateTimeFormatter formatter) {
        String[] dates = dateStr.split(DATE_SPLIT_REGEX);
        throwExceptionInCaseOfInvalidSizeOfArrayWithDates(dates);
        final String startDateStr = dates[0].trim();
        final String endDateStr = dates[1].trim();
        return Pair.of(parseLocalDate(startDateStr, formatter), parseLocalDate(endDateStr, formatter));
    }

    private static void throwExceptionInCaseOfInvalidSizeOfArrayWithDates(final String[] dates) {
        if (dates.length != EXPECTED_ARRAY_DATES_SIZE) {
            final String exceptionMessage = String.format(INVALID_ARRAY_DATES_SIZE, dates.length);
            log.error(exceptionMessage);
            throw new DateConversionException(exceptionMessage);
        }
    }

    private static LocalDate parseLocalDate(final String dateStr, final DateTimeFormatter formatter) {
        LocalDate localDate = null;
        String dateWithYear = dateStr + SPACE + LocalDate.now().getYear();
        EventDateLocale[] dateLocales = EventDateLocale.values();
        for (int i = 0; i < dateLocales.length && localDate == null; i++) {
            try {
                localDate = LocalDate.parse(dateWithYear, formatter.withLocale(dateLocales[i].getLocale()));
            } catch (Exception e) {
                if (i == dateLocales.length - 1) {
                    throwExceptionInCaseOfDateCannotBeConvert(dateWithYear, e);
                }
            }
        }
        return localDate;
    }

    private static void throwExceptionInCaseOfDateCannotBeConvert(final String date, final Exception e) {
        final String exceptionMessage = String.format(ERROR_DURING_DATE_CONVERSION, date);
        log.error(exceptionMessage);
        throw new DateConversionException(exceptionMessage, e);
    }

    @Getter
    @ToString
    public enum EventDateLocale {
        ENGLISH(Locale.ENGLISH), RUSSIAN(new Locale("ru"));

        private final Locale locale;

        EventDateLocale(Locale locale) {
            this.locale = locale;
        }
    }
}
