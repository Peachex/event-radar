package by.klevitov.eventparser.util;

import by.klevitov.eventradarcommon.dto.EventDate;
import by.klevitov.eventradarcommon.dto.EventPrice;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class EventCreatorUtil {
    private EventCreatorUtil() {
    }

    public static EventDate createEventDate(final String startDateStr, final String endDateStr) {
        LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
        LocalDate endDate = endDateStr != null ? LocalDate.parse(endDateStr) : null;
        return new EventDate(startDate, endDate);
    }

    public static EventPrice createEventPrice(final String minPriceStr, final String maxPriceStr) {
        BigDecimal minPrice = minPriceStr != null ? new BigDecimal(minPriceStr) : null;
        BigDecimal maxPrice = maxPriceStr != null ? new BigDecimal(maxPriceStr) : null;
        return new EventPrice(minPrice, maxPrice);
    }
}
