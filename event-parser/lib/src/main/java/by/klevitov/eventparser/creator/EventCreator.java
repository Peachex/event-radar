package by.klevitov.eventparser.creator;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.EventDate;

import java.time.LocalDate;
import java.util.Map;

public interface EventCreator {
    AbstractEventDTO create(final Map<String, String> fields);

    static EventDate createEventDate(final String startDateStr, final String endDateStr) {
        LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
        LocalDate endDate = endDateStr != null ? LocalDate.parse(endDateStr) : null;
        return new EventDate(startDate, endDate);
    }
}
