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

import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_DATE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
public final class EventParserUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("[до d MMMM yyyy]"))
            .append(DateTimeFormatter.ofPattern("[с d MMMM yyyy]"))
            .append(DateTimeFormatter.ofPattern("[d MMMM yyyy]"))
            .toFormatter();

    private EventParserUtil() {
    }

    public static Pair<LocalDate, LocalDate> convertDateFromStringToLocalDate(final String dateStr) {
        throwExceptionInCaseOfEmptyDate(dateStr);
        return (dateIsSingle(dateStr) ? convertSingleDate(dateStr) : convertDualDate(dateStr));
    }

    private static void throwExceptionInCaseOfEmptyDate(final String date) {
        if (isEmpty(date)) {
            final String exceptionMessage = String.format(NULL_OR_EMPTY_DATE, date);
            log.error(exceptionMessage);
            throw new DateConversionException(exceptionMessage);
        }
    }

    private static boolean dateIsSingle(final String date) {
        //todo add logic.
        return true;
    }

    private static Pair<LocalDate, LocalDate> convertSingleDate(final String dateStr) {
        //todo add logic.
        return null;
    }

    private static Pair<LocalDate, LocalDate> convertDualDate(final String dateStr) {
        //todo add logic.
        return null;
    }

    @Getter
    @ToString
    private enum EventDateLocale {
        ENGLISH(new Locale("en")), RUSSIAN(new Locale("ru"));

        private final Locale locale;

        EventDateLocale(Locale locale) {
            this.locale = locale;
        }
    }
}
