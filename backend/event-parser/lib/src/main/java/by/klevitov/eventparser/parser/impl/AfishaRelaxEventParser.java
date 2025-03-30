package by.klevitov.eventparser.parser.impl;

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
import static by.klevitov.eventparser.constant.EventField.SOURCE_TYPE;
import static by.klevitov.eventparser.constant.EventField.TITLE;
import static by.klevitov.eventparser.constant.EventLocation.BELARUS;
import static by.klevitov.eventparser.constant.EventLocation.MINSK;
import static by.klevitov.eventparser.constant.HTMLSiteElement.AFISHA_RELAX_CATEGORY;
import static by.klevitov.eventparser.constant.HTMLSiteElement.AFISHA_RELAX_DATA_SCHEMA;
import static by.klevitov.eventparser.constant.HTMLSiteElement.AFISHA_RELAX_DATE;
import static by.klevitov.eventparser.constant.HTMLSiteElement.AFISHA_RELAX_EVENT_LINK;
import static by.klevitov.eventparser.constant.HTMLSiteElement.AFISHA_RELAX_IMAGE_LINK;
import static by.klevitov.eventparser.constant.HTMLSiteElement.AFISHA_RELAX_IMAGE_LINK_SRC;
import static by.klevitov.eventparser.constant.HTMLSiteElement.AFISHA_RELAX_MAIN_ITEM;
import static by.klevitov.eventparser.constant.HTMLSiteElement.AFISHA_RELAX_TITLE;
import static by.klevitov.eventparser.constant.PropertyConstant.AFISHA_RELAX_SITE_URL;
import static by.klevitov.eventparser.constant.PropertyConstant.PROPERTY_FILE_WITH_SITES_FOR_PARSING;
import static by.klevitov.eventparser.util.EventParserUtil.parseDateAndAddToMap;

@Log4j2
public class AfishaRelaxEventParser implements EventParser {
    private static final String AFISHA_SITE_URL = PropertyUtil.retrieveProperty(AFISHA_RELAX_SITE_URL,
            PROPERTY_FILE_WITH_SITES_FOR_PARSING);

    private static final EventSourceType AFISHA_RELAX_SOURCE_TYPE = EventSourceType.AFISHA_RELAX;

    private final EventCreator eventCreator;

    public AfishaRelaxEventParser(final EventCreator eventCreator) {
        this.eventCreator = eventCreator;
    }

    @Override
    public List<AbstractEventDTO> parse(final Document htmlDocument) {
        List<AbstractEventDTO> events = new ArrayList<>();
        Elements elements = htmlDocument.getElementsByClass(AFISHA_RELAX_MAIN_ITEM);
        for (Element element : elements) {
            Map<String, String> fields = createFieldsMap(element);
            AbstractEventDTO event = eventCreator.create(fields);
            events.add(event);
        }
        return events;
    }

    private static Map<String, String> createFieldsMap(final Element element) {
        Map<String, String> fields = new HashMap<>();
        Elements itemElements = element.getElementsByAttribute(AFISHA_RELAX_DATA_SCHEMA);
        fields.put(TITLE, itemElements.attr(AFISHA_RELAX_TITLE));
        fields.put(LOCATION_COUNTRY, BELARUS);
        fields.put(LOCATION_CITY, MINSK);
        fields.put(CATEGORY, itemElements.attr(AFISHA_RELAX_CATEGORY));
        fields.put(SOURCE_TYPE, AFISHA_RELAX_SOURCE_TYPE.name());
        fields.put(DATE_STR, element.getElementsByClass(AFISHA_RELAX_DATE).text());
        fields.put(EVENT_LINK, itemElements.attr(AFISHA_RELAX_EVENT_LINK));
        fields.put(IMAGE_LINK, element.getElementsByClass(AFISHA_RELAX_IMAGE_LINK).attr(AFISHA_RELAX_IMAGE_LINK_SRC));
        parseDateAndAddToMap(fields);
        return fields;
    }

    @Override
    public String retrieveSiteURL() {
        return AFISHA_SITE_URL;
    }
}
