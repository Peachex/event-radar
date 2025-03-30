package by.klevitov.eventparser.creator;

import by.klevitov.eventparser.creator.impl.ByCardEventCreator;
import by.klevitov.eventparser.util.EventCreatorUtil;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.EventDate;
import by.klevitov.eventradarcommon.dto.EventPrice;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static by.klevitov.eventparser.constant.EventField.CATEGORY;
import static by.klevitov.eventparser.constant.EventField.DATE_STR;
import static by.klevitov.eventparser.constant.EventField.END_DATE;
import static by.klevitov.eventparser.constant.EventField.EVENT_LINK;
import static by.klevitov.eventparser.constant.EventField.IMAGE_LINK;
import static by.klevitov.eventparser.constant.EventField.LOCATION_CITY;
import static by.klevitov.eventparser.constant.EventField.LOCATION_COUNTRY;
import static by.klevitov.eventparser.constant.EventField.MAX_PRICE;
import static by.klevitov.eventparser.constant.EventField.MIN_PRICE;
import static by.klevitov.eventparser.constant.EventField.PRICE_STR;
import static by.klevitov.eventparser.constant.EventField.SOURCE_TYPE;
import static by.klevitov.eventparser.constant.EventField.START_DATE;
import static by.klevitov.eventparser.constant.EventField.TITLE;
import static by.klevitov.eventparser.util.EventCreatorUtil.createEventDate;
import static by.klevitov.eventparser.util.EventCreatorUtil.createEventPrice;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByCardEventCreatorTest {
    private static final EventCreator eventCreator = new ByCardEventCreator();
    private static Map<String, String> fields;

    @BeforeEach
    public void init() {
        fields = new HashMap<>();
        fields.put(TITLE, "title");
        fields.put(LOCATION_COUNTRY, "country");
        fields.put(LOCATION_CITY, "city");
        fields.put(CATEGORY, "category");
        fields.put(SOURCE_TYPE, "BYCARD");
        fields.put(DATE_STR, "dateStr");
        fields.put(START_DATE, "startDate");
        fields.put(END_DATE, "endDate");
        fields.put(PRICE_STR, "priceStr");
        fields.put(MIN_PRICE, "minPrice");
        fields.put(MAX_PRICE, "maxPrice");
        fields.put(EVENT_LINK, "eventLink");
        fields.put(IMAGE_LINK, "imageLink");
    }

    @Test
    public void test_create() {
        try (MockedStatic<EventCreatorUtil> eventCreatorUtil = Mockito.mockStatic(EventCreatorUtil.class)) {
            eventCreatorUtil.when(() -> createEventDate(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(new EventDate(LocalDate.of(2024, 7, 13),
                            LocalDate.of(2024, 7, 16)));
            eventCreatorUtil.when(() -> createEventPrice(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(new EventPrice(new BigDecimal("12.34"), new BigDecimal("567.89")));

            AbstractEventDTO expected = ByCardEventDTO.builder()
                    .title(fields.get(TITLE))
                    .location(new LocationDTO(fields.get(LOCATION_COUNTRY), fields.get(LOCATION_CITY)))
                    .category(fields.get(CATEGORY))
                    .sourceType(EventSourceType.valueOf(fields.get(SOURCE_TYPE)))
                    .dateStr(fields.get(DATE_STR))
                    .date(new EventDate(LocalDate.of(2024, 7, 13), LocalDate.of(2024, 7, 16)))
                    .priceStr(fields.get(PRICE_STR))
                    .price(new EventPrice(new BigDecimal("12.34"), new BigDecimal("567.89")))
                    .eventLink(fields.get(EVENT_LINK))
                    .imageLink(fields.get(IMAGE_LINK))
                    .build();

            AbstractEventDTO actual = eventCreator.create(fields);
            eventCreatorUtil.verify(() -> createEventDate(Mockito.anyString(), Mockito.anyString()));
            assertEquals(expected, actual);
        }
    }
}
