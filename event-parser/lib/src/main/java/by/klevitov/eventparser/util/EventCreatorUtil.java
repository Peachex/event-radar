package by.klevitov.eventparser.util;

import by.klevitov.eventradarcommon.dto.EventDate;

import java.time.LocalDate;

public final class EventCreatorUtil {
    private EventCreatorUtil() {
    }

    public static EventDate createEventDate(final String startDateStr, final String endDateStr) {
        LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
        LocalDate endDate = endDateStr != null ? LocalDate.parse(endDateStr) : null;
        return new EventDate(startDate, endDate);
    }
}
