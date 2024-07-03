package by.klevitov.eventparser.creator;

import by.klevitov.eventparser.creator.impl.ByCardEventCreator;
import by.klevitov.eventparser.dto.AbstractEventDTO;
import by.klevitov.eventparser.dto.ByCardEventDTO;
import by.klevitov.eventparser.dto.EventSourceType;
import by.klevitov.eventparser.dto.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static by.klevitov.eventparser.constant.EventField.CATEGORY;
import static by.klevitov.eventparser.constant.EventField.DATE_STR;
import static by.klevitov.eventparser.constant.EventField.EVENT_LINK;
import static by.klevitov.eventparser.constant.EventField.IMAGE_LINK;
import static by.klevitov.eventparser.constant.EventField.LOCATION_CITY;
import static by.klevitov.eventparser.constant.EventField.LOCATION_COUNTRY;
import static by.klevitov.eventparser.constant.EventField.PRICE_STR;
import static by.klevitov.eventparser.constant.EventField.SOURCE_TYPE;
import static by.klevitov.eventparser.constant.EventField.TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByCardEventCreatorTest {
    private static EventCreator eventCreator;
    private static Map<String, String> fields;

    @BeforeAll
    public static void init() {
        eventCreator = new ByCardEventCreator();
        fields = Map.of(
                TITLE, "title",
                LOCATION_COUNTRY, "country",
                LOCATION_CITY, "city",
                CATEGORY, "category",
                SOURCE_TYPE, "AFISHA_RELAX",
                DATE_STR, "dateStr",
                PRICE_STR, "priceStr",
                EVENT_LINK, "eventLink",
                IMAGE_LINK, "imageLink"
        );
    }

    @Test
    public void test_create() {
        AbstractEventDTO expected = ByCardEventDTO.builder()
                .title(fields.get(TITLE))
                .location(new Location(fields.get(LOCATION_COUNTRY), fields.get(LOCATION_CITY)))
                .category(fields.get(CATEGORY))
                .sourceType(EventSourceType.valueOf(fields.get(SOURCE_TYPE)))
                .dateStr(fields.get(DATE_STR))
                .priceStr(fields.get(PRICE_STR))
                .eventLink(fields.get(EVENT_LINK))
                .imageLink(fields.get(IMAGE_LINK))
                .build();
        AbstractEventDTO actual = eventCreator.create(fields);
        assertEquals(expected, actual);
    }
}
