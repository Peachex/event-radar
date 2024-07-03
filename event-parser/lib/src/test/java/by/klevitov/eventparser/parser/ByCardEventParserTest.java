package by.klevitov.eventparser.parser;

import by.klevitov.eventparser.creator.EventCreator;
import by.klevitov.eventparser.dto.AbstractEventDTO;
import by.klevitov.eventparser.dto.ByCardEventDTO;
import by.klevitov.eventparser.exception.HTMLDocumentRetrievingException;
import by.klevitov.eventparser.parser.impl.ByCardEventParser;
import by.klevitov.eventparser.util.JsoupUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Method;
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
import static by.klevitov.eventparser.dto.EventSourceType.BYCARD;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ByCardEventParserTest {
    private static EventParser parser;
    @Mock
    private static EventCreator creator;

    @BeforeAll
    public static void init() {
        creator = Mockito.mock();
        parser = new ByCardEventParser(creator);
    }

    @Test
    public void test_parse() throws HTMLDocumentRetrievingException {
        Mockito.when(creator.create(Mockito.anyMap())).thenReturn(new ByCardEventDTO());
        Document htmlDocument = JsoupUtil.retrieveDocumentByURL("https://bycard.by");
        List<AbstractEventDTO> events = parser.parse(htmlDocument);
        assertTrue(isNotEmpty(events));
    }

    @Test
    public void test_createFieldsMap() throws Exception {
        Method privateMethod = ByCardEventParser.class.getDeclaredMethod("createFieldsMap", Element.class,
                String.class);
        privateMethod.setAccessible(true);
        Element element = createElement();
        Map<String, String> expected = createExpectedMap();
        Object actual = privateMethod.invoke(parser, element, "category");
        assertEquals(expected, actual);
    }

    private Element createElement() {
        Element element = new Element("tag");
        element.append("<a href=\"/afisha/minsk/top/968360\" class=\"capsule\"><div class=\"capsule__image\"><img " +
                "src=\"imageLink\" srcset=\"srcset\"><p class=\"capsule__title\">title</p><p class=\"capsule__date\">" +
                "<span/></p><p class=\"capsule__date\"><span>dateStr</span></p><p class=\"capsule__price\">" +
                "<span>priceStr</span></p></a>");
        return element;
    }

    private Map<String, String> createExpectedMap() {
        return Map.of(
                TITLE, "title",
                LOCATION_COUNTRY, BELARUS,
                LOCATION_CITY, MINSK,
                CATEGORY, "category",
                SOURCE_TYPE, BYCARD.name(),
                DATE_STR, "dateStr",
                PRICE_STR, "priceStr",
                EVENT_LINK, "https://bycard.by",
                IMAGE_LINK, "imageLink"
        );
    }
}
