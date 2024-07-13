package by.klevitov.eventparser.creator.impl;

import by.klevitov.eventparser.creator.EventCreator;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import by.klevitov.eventradarcommon.dto.Location;

import java.util.Map;

import static by.klevitov.eventparser.constant.EventField.CATEGORY;
import static by.klevitov.eventparser.constant.EventField.DATE_STR;
import static by.klevitov.eventparser.constant.EventField.END_DATE;
import static by.klevitov.eventparser.constant.EventField.EVENT_LINK;
import static by.klevitov.eventparser.constant.EventField.IMAGE_LINK;
import static by.klevitov.eventparser.constant.EventField.LOCATION_CITY;
import static by.klevitov.eventparser.constant.EventField.LOCATION_COUNTRY;
import static by.klevitov.eventparser.constant.EventField.PRICE_STR;
import static by.klevitov.eventparser.constant.EventField.SOURCE_TYPE;
import static by.klevitov.eventparser.constant.EventField.START_DATE;
import static by.klevitov.eventparser.constant.EventField.TITLE;
import static by.klevitov.eventparser.util.EventCreatorUtil.createEventDate;

public class ByCardEventCreator implements EventCreator {
    @Override
    public AbstractEventDTO create(final Map<String, String> fields) {
        return ByCardEventDTO.builder()
                .title(fields.get(TITLE))
                .location(new Location(fields.get(LOCATION_COUNTRY), fields.get(LOCATION_CITY)))
                .category(fields.get(CATEGORY))
                .sourceType(EventSourceType.valueOf(fields.get(SOURCE_TYPE)))
                .dateStr(fields.get(DATE_STR))
                .date(createEventDate(fields.get(START_DATE), fields.get(END_DATE)))
                .priceStr(fields.get(PRICE_STR))
                .eventLink(fields.get(EVENT_LINK))
                .imageLink(fields.get(IMAGE_LINK))
                .build();
    }
}
