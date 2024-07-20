package by.klevitov.eventparser.parser.impl;

import by.klevitov.eventparser.constant.PropertyConstant;
import by.klevitov.eventparser.creator.EventCreator;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.util.PropertyUtil;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import static by.klevitov.eventparser.constant.EventLocation.BELARUS;
import static by.klevitov.eventparser.constant.EventLocation.MINSK;
import static by.klevitov.eventparser.constant.HTMLSiteElement.BYCARD_CAPSULE_MAIN_ELEMENT;
import static by.klevitov.eventparser.constant.HTMLSiteElement.BYCARD_CATEGORY;
import static by.klevitov.eventparser.constant.HTMLSiteElement.BYCARD_DATE;
import static by.klevitov.eventparser.constant.HTMLSiteElement.BYCARD_EVENTS_ROW;
import static by.klevitov.eventparser.constant.HTMLSiteElement.BYCARD_EVENT_LINK_HREF;
import static by.klevitov.eventparser.constant.HTMLSiteElement.BYCARD_IMAGE_LINK;
import static by.klevitov.eventparser.constant.HTMLSiteElement.BYCARD_IMAGE_LINK_SRC;
import static by.klevitov.eventparser.constant.HTMLSiteElement.BYCARD_PRICE;
import static by.klevitov.eventparser.constant.HTMLSiteElement.BYCARD_TITLE;
import static by.klevitov.eventparser.constant.PropertyConstant.PROPERTY_FILE_WITH_SITES_FOR_PARSING;
import static by.klevitov.eventparser.util.ByCardEventParserUtil.parsePriceAndAddToMap;
import static by.klevitov.eventparser.util.EventParserUtil.parseDateAndAddToMap;

@Log4j2
public class ByCardEventParser implements EventParser {
    private static final String BYCARD_SITE_URL = PropertyUtil.retrieveProperty(PropertyConstant.BYCARD_SITE_URL,
            PROPERTY_FILE_WITH_SITES_FOR_PARSING);

    private static final EventSourceType BYCARD_SOURCE_TYPE = EventSourceType.BYCARD;

    private final EventCreator eventCreator;

    public ByCardEventParser(final EventCreator eventCreator) {
        this.eventCreator = eventCreator;
    }

    @Override
    public List<AbstractEventDTO> parse(final Document htmlDocument) {
        List<AbstractEventDTO> events = new ArrayList<>();
        Elements elements = htmlDocument.getElementsByClass(BYCARD_EVENTS_ROW);
        for (Element element : elements) {
            String category = element.getElementsByClass(BYCARD_CATEGORY).text();
            Elements innerElements = element.getElementsByClass(BYCARD_CAPSULE_MAIN_ELEMENT);
            events.addAll(createEventsFromInnerElements(innerElements, category));
        }
        return events;
    }

    private List<AbstractEventDTO> createEventsFromInnerElements(final Elements innerElements, final String category) {
        List<AbstractEventDTO> events = new ArrayList<>();
        for (Element innerElement : innerElements) {
            Map<String, String> fields = createFieldsMap(innerElement, category);
            AbstractEventDTO event = eventCreator.create(fields);
            events.add(event);
        }
        return events;
    }

    private static Map<String, String> createFieldsMap(final Element element, String category) {
        Map<String, String> fields = new HashMap<>();
        fields.put(TITLE, element.getElementsByClass(BYCARD_TITLE).text());
        fields.put(LOCATION_COUNTRY, BELARUS);
        fields.put(LOCATION_CITY, MINSK);
        fields.put(CATEGORY, category);
        fields.put(SOURCE_TYPE, BYCARD_SOURCE_TYPE.name());
        fields.put(DATE_STR, element.getElementsByClass(BYCARD_DATE).text());
        fields.put(PRICE_STR, element.getElementsByClass(BYCARD_PRICE).text());
        fields.put(EVENT_LINK, BYCARD_SITE_URL + element.attr(BYCARD_EVENT_LINK_HREF));
        fields.put(IMAGE_LINK, element.getElementsByClass(BYCARD_IMAGE_LINK).get(0)
                .getElementsByAttribute(BYCARD_IMAGE_LINK_SRC).attr(BYCARD_IMAGE_LINK_SRC));
        parseDateAndAddToMap(fields);
        parsePriceAndAddToMap(fields);
        return fields;
    }

    @Override
    public String retrieveSiteURL() {
        return BYCARD_SITE_URL;
    }
}
