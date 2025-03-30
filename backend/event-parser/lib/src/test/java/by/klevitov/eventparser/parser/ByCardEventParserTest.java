package by.klevitov.eventparser.parser;

import by.klevitov.eventparser.creator.EventCreator;
import by.klevitov.eventparser.parser.impl.ByCardEventParser;
import by.klevitov.eventparser.util.ByCardEventParserUtil;
import by.klevitov.eventparser.util.EventParserUtil;
import by.klevitov.eventparser.util.PropertyUtil;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
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
import static by.klevitov.eventparser.constant.EventLocation.BELARUS;
import static by.klevitov.eventparser.constant.EventLocation.MINSK;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByCardEventParserTest {
    private static EventParser parser;
    @Mock
    private static EventCreator creator;

    @BeforeEach
    public void reset() {
        Mockito.clearAllCaches();
        creator = Mockito.mock();
        parser = new ByCardEventParser(creator);
    }

    @Test
    public void test_parse() {
        Mockito.when(creator.create(Mockito.anyMap())).thenReturn(new ByCardEventDTO());
        String htmlPageStr = createPageHTMLStr();
        Document htmlDocument = Jsoup.parse(htmlPageStr);
        List<AbstractEventDTO> events = parser.parse(htmlDocument);
        assertEquals(2, events.size());
    }

    @Test
    public void test_createFieldsMap() throws Exception {
        try (MockedStatic<EventParserUtil> parserUtil = Mockito.mockStatic(EventParserUtil.class);
             MockedStatic<ByCardEventParserUtil> byCardParserUtil = Mockito.mockStatic(ByCardEventParserUtil.class)) {
            parserUtil.when(() -> EventParserUtil.parseDateAndAddToMap(Mockito.anyMap()))
                    .thenAnswer(invocationOnMock -> {
                        Map<String, String> fields = invocationOnMock.getArgument(0);
                        fields.put(START_DATE, "2024-06-11");
                        fields.put(END_DATE, "2024-12-31");
                        return null;
                    });
            byCardParserUtil.when(() -> ByCardEventParserUtil.parsePriceAndAddToMap(Mockito.anyMap()))
                    .thenAnswer(invocationOnMock -> {
                        Map<String, String> fields = invocationOnMock.getArgument(0);
                        fields.put(MIN_PRICE, "40.00");
                        fields.put(MAX_PRICE, "250.00");
                        return null;
                    });

            Method privateMethod = ByCardEventParser.class.getDeclaredMethod("createFieldsMap", Element.class,
                    String.class);
            privateMethod.setAccessible(true);
            Element element = createElement();
            Map<String, String> expected = createExpectedMap();
            Object actual = privateMethod.invoke(parser, element, "category");
            assertEquals(expected, actual);
        }
    }

    @Test
    public void test_retrieveSiteURL() throws Exception {
        try (MockedStatic<PropertyUtil> propertyUtil = Mockito.mockStatic(PropertyUtil.class)) {
            propertyUtil.when(() -> PropertyUtil.retrieveProperty(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn("https://bycard.by");
            Field privatField = ByCardEventParser.class.getDeclaredField("BYCARD_SITE_URL");
            privatField.setAccessible(true);
            Object expected = privatField.get(null);
            String actual = parser.retrieveSiteURL();
            assertEquals(expected, actual);
        }
    }

    private Element createElement() {
        Element element = new Element("tag");
        element.append("<a href=\"/afisha/minsk/top/968360\" class=\"capsule\"><div class=\"capsule__image\"><img " +
                "src=\"imageLink\" srcset=\"srcset\"><p class=\"capsule__title\">title</p><p class=\"capsule__date\">" +
                "<span/></p><p class=\"capsule__date\"><span>11 июня - 31 декабря</span></p><p " +
                "class=\"capsule__price\"><span>от 40.00 - до 250.00 руб.</span></p></a>");
        return element;
    }

    private Map<String, String> createExpectedMap() {
        Map<String, String> map = new HashMap<>();
        map.put(TITLE, "title");
        map.put(LOCATION_COUNTRY, BELARUS);
        map.put(LOCATION_CITY, MINSK);
        map.put(CATEGORY, "category");
        map.put(SOURCE_TYPE, BYCARD.name());
        map.put(DATE_STR, "11 июня - 31 декабря");
        map.put(PRICE_STR, "от 40.00 - до 250.00 руб.");
        map.put(EVENT_LINK, "https://bycard.by");
        map.put(IMAGE_LINK, "imageLink");
        map.put(START_DATE, "2024-06-11");
        map.put(END_DATE, "2024-12-31");
        map.put(MIN_PRICE, "40.00");
        map.put(MAX_PRICE, "250.00");
        return map;
    }

    private String createPageHTMLStr() {
        return "<html data-n-head-ssr lang=\"en\" data-n-head=\"lang\"><head><title>Test</title></head><body>" +
                "<events-row class=\"events-row\"><div class=\"events-row__headline\">\n" +
                "\tTest\n\t</div><a href=\"/afisha/minsk/top/123\" class=\"capsule\"><p class=\"capsule__title\">" +
                "Test</p><p class=\"capsule__date\"><span>11 июня - 31 декабря</span></p><p " +
                "class=\"capsule__price\"><span>Test</span></p><div class=\"capsule__image\"><img " +
                "src=\"https://webgate.24guru.by/uploads/events/thumbs/170x240/7BdeRnLTg.png\"/></div></a>" +
                "</events-row><events-row class=\"events-row\"><div class=\"events-row__headline\">\n" +
                "Test\n\t\t\t</div><a href=\"/afisha/minsk/top/456\" class=\"capsule\"><p class=\"capsule__title" +
                "\">Test</p><p class=\"capsule__date\"><span> Test</span></p><p class=\"capsule__price\">" +
                "<span>Test</span></p><div class=\"capsule__image\"><img src=\"" +
                "https://webgate.24guru.by/uploads/events/thumbs/170x240/75YPYZywa.jpg\"/></div></a>" +
                "</events-row></body></html>";
    }
}
