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

import static by.klevitov.eventparser.constant.ExceptionMessage.ERROR_DURING_DATE_CONVERSION;
import static by.klevitov.eventparser.constant.ExceptionMessage.INVALID_ARRAY_DATES_SIZE;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_DATE;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
public final class EventParserUtil {
    private static final String DATE_SPLIT_REGEX = "-";
    private static final String START_DATE_PREFIX = "c";

    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("[до d MMMM yyyy]"))
            .append(DateTimeFormatter.ofPattern("[с d MMMM yyyy]"))
            .append(DateTimeFormatter.ofPattern("[d MMMM yyyy]"))
            .toFormatter();

    private EventParserUtil() {
    }

    public static Pair<LocalDate, LocalDate> convertDateToLocalDate(final String dateStr,
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
        if (dates.length != 2) {
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
                    throwExceptionInCaseOfDateCannotBeConvert(dateStr, e);
                }
            }
        }
        return localDate;
    }

    private static void throwExceptionInCaseOfDateCannotBeConvert(final String dateStr, final Exception e) {
        final String exceptionMessage = String.format(ERROR_DURING_DATE_CONVERSION, dateStr);
        log.error(exceptionMessage);
        throw new DateConversionException(exceptionMessage, e);
    }

    @Getter
    @ToString
    private enum EventDateLocale {
        ENGLISH(Locale.ENGLISH), RUSSIAN(new Locale("ru"));

        private final Locale locale;

        EventDateLocale(Locale locale) {
            this.locale = locale;
        }
    }
}
