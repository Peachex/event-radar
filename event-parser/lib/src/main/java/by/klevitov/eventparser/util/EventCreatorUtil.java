package by.klevitov.eventparser.util;

import by.klevitov.eventradarcommon.dto.EventDateDTO;
import by.klevitov.eventradarcommon.dto.EventDateDTO;
import by.klevitov.eventradarcommon.dto.EventPriceDTO;
import by.klevitov.eventradarcommon.dto.EventPriceDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class EventCreatorUtil {
    private EventCreatorUtil() {
    }

    public static EventDateDTO createEventDate(final String startDateStr, final String endDateStr) {
        LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
        LocalDate endDate = endDateStr != null ? LocalDate.parse(endDateStr) : null;
        return new EventDateDTO(startDate, endDate);
    }

    public static EventPriceDTO createEventPrice(final String minPriceStr, final String maxPriceStr) {
        BigDecimal minPrice = minPriceStr != null ? new BigDecimal(minPriceStr) : null;
        BigDecimal maxPrice = maxPriceStr != null ? new BigDecimal(maxPriceStr) : null;
        return new EventPriceDTO(minPrice, maxPrice);
    }
}
