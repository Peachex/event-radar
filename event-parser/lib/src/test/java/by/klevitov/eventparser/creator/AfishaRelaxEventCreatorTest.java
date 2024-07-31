package by.klevitov.eventparser.creator;

import by.klevitov.eventparser.creator.impl.AfishaRelaxEventCreator;
import by.klevitov.eventparser.util.EventCreatorUtil;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.EventDateDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Map;

import static by.klevitov.eventparser.constant.EventField.CATEGORY;
import static by.klevitov.eventparser.constant.EventField.DATE_STR;
import static by.klevitov.eventparser.constant.EventField.END_DATE;
import static by.klevitov.eventparser.constant.EventField.EVENT_LINK;
import static by.klevitov.eventparser.constant.EventField.IMAGE_LINK;
import static by.klevitov.eventparser.constant.EventField.LOCATION_CITY;
import static by.klevitov.eventparser.constant.EventField.LOCATION_COUNTRY;
import static by.klevitov.eventparser.constant.EventField.SOURCE_TYPE;
import static by.klevitov.eventparser.constant.EventField.START_DATE;
import static by.klevitov.eventparser.constant.EventField.TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AfishaRelaxEventCreatorTest {
    private static EventCreator eventCreator;
    private static Map<String, String> fields;

    @BeforeAll
    public static void init() {
        eventCreator = new AfishaRelaxEventCreator();
        fields = Map.of(
                TITLE, "title",
                LOCATION_COUNTRY, "country",
                LOCATION_CITY, "city",
                CATEGORY, "category",
                SOURCE_TYPE, "AFISHA_RELAX",
                DATE_STR, "dateStr",
                START_DATE, "startDate",
                END_DATE, "endDate",
                EVENT_LINK, "eventLink",
                IMAGE_LINK, "imageLink"
        );
    }

    @Test
    public void test_create() {
        try (MockedStatic<EventCreatorUtil> creatorUtil = Mockito.mockStatic(EventCreatorUtil.class)) {
            creatorUtil.when(() -> EventCreatorUtil.createEventDate(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(new EventDateDTO(LocalDate.of(2024, 7, 13),
                            LocalDate.of(2024, 7, 16)));
            AbstractEventDTO expected = AfishaRelaxEventDTO.builder()
                    .title(fields.get(TITLE))
                    .location(new LocationDTO(fields.get(LOCATION_COUNTRY), fields.get(LOCATION_CITY)))
                    .category(fields.get(CATEGORY))
                    .sourceType(EventSourceType.valueOf(fields.get(SOURCE_TYPE)))
                    .dateStr(fields.get(DATE_STR))
                    .date(new EventDateDTO(LocalDate.of(2024, 7, 13), LocalDate.of(2024, 7, 16)))
                    .eventLink(fields.get(EVENT_LINK))
                    .imageLink(fields.get(IMAGE_LINK))
                    .build();
            AbstractEventDTO actual = eventCreator.create(fields);
            creatorUtil.verify(() -> EventCreatorUtil.createEventDate(Mockito.anyString(), Mockito.anyString()));
            assertEquals(expected, actual);
        }
    }
}
