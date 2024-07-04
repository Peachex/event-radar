package by.klevitov.eventparser.parser;

import by.klevitov.eventparser.creator.EventCreator;
import by.klevitov.eventparser.dto.AbstractEventDTO;
import by.klevitov.eventparser.dto.AfishaRelaxEventDTO;
import by.klevitov.eventparser.parser.impl.AfishaRelaxEventParser;
import by.klevitov.eventparser.util.PropertyUtil;
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
import static by.klevitov.eventparser.dto.EventSourceType.AFISHA_RELAX;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AfishaRelaxEventParserTest {
    private static EventParser parser;
    @Mock
    private static EventCreator creator;

    @BeforeEach
    public void reset() {
        Mockito.clearAllCaches();
        creator = Mockito.mock();
        parser = new AfishaRelaxEventParser(creator);
    }

    @Test
    public void test_parse() {
        Mockito.when(creator.create(Mockito.anyMap())).thenReturn(new AfishaRelaxEventDTO());
        String htmlPageStr = createPageHTMLStr();
        Document htmlDocument = Jsoup.parse(htmlPageStr);
        List<AbstractEventDTO> events = parser.parse(htmlDocument);
        assertEquals(2, events.size());
    }

    @Test
    public void test_createFieldsMap() throws Exception {
        Method privateMethod = AfishaRelaxEventParser.class.getDeclaredMethod("createFieldsMap", Element.class);
        privateMethod.setAccessible(true);
        Element element = createElement();
        Map<String, String> expected = createExpectedMap();
        Object actual = privateMethod.invoke(parser, element);
        assertEquals(expected, actual);
    }

    @Test
    public void test_retrieveSiteURL() throws Exception {
        try (MockedStatic<PropertyUtil> propertyUtil = Mockito.mockStatic(PropertyUtil.class)) {
            propertyUtil.when(() -> PropertyUtil.retrieveProperty(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn("https://afisha.relax.by");
            Field privatField = AfishaRelaxEventParser.class.getDeclaredField("AFISHA_SITE_URL");
            privatField.setAccessible(true);
            Object expected = privatField.get(null);
            String actual = parser.retrieveSiteURL();
            assertEquals(expected, actual);
        }
    }

    private Element createElement() {
        Element element = new Element("tag");
        element.append("<div class=\"b-afisha-layout_strap--item\"><div class=\"b-afisha_blocks-strap_item_lnk_img\">" +
                "<a href=\"eventLink\" " + "data-id=\"11117297\" data-name=\"title\" data-schema-title=\"category\" " +
                "data-index=\"1\" data-position=\"event_main_page\" class=\"b-afisha_blocks-strap_item_lnk\"><i " +
                "class=\"b-afisha_blocks-movies_item_lnk_premier\">a</i><img height=\"200\" class=\"b-afisha_" +
                "blocks-movies_item_img \" src=\"imageLink\" " + "alt=\"<? HtmlHelper::encode($event['title']) ?>\"> " +
                "</a><a class=\"btn btn--xs btn--theme-2 " + "text-uppercase\" href=\"https://afisha.relax.by/kino/" +
                "11117297-tri-bogatyrya-ni-dnya-bez-podviga/minsk/" + "\">a</a></div><span class=\"" +
                "b-afisha-layout_maldives_strap_date\">dateStr</span><a href=\"" + "https://afisha.relax.by/kino/" +
                "11117297-tri-bogatyrya-ni-dnya-bez-podviga/minsk/\" class=\"b-afisha_" + "blocks-strap_item_lnk_txt " +
                "link\">a</a></div>");
        return element;
    }

    private Map<String, String> createExpectedMap() {
        return Map.of(
                TITLE, "title",
                LOCATION_COUNTRY, BELARUS,
                LOCATION_CITY, MINSK,
                CATEGORY, "category",
                SOURCE_TYPE, AFISHA_RELAX.name(),
                DATE_STR, "dateStr",
                EVENT_LINK, "eventLink",
                IMAGE_LINK, "imageLink"
        );
    }

    private static String createPageHTMLStr() {
        return "<html lang=\"ru\" class=\" is-response relaxby\"><head><title>Test</title></head><body class=" +
                "\"afisha\"><div class=\"b-afisha-layout_strap--item\"><div class=\"b-afisha_blocks-strap\n" +
                "_item_lnk_img\"><a href=\"https://afisha.relax.by/kino/11117297-tri-bogatyrya-ni-dnya-bez-po\n" +
                "dviga/minsk/\" data-id=\"11117297\" data-name=\"Test\" data-sche ma-title=\"Test\" " +
                "data-index=\"1\" data-position=\"event_main_page\" class=\"b-afisha_block\n" +
                "s-strap_item_lnk\"><i class=\"b-afisha_blocks-movies_item_lnk_premier\">Test</i><img he " +
                "ight=\"200\" class=\"b-afisha_blocks-movies_item_img \" src=\"https://ms1.relax.by/images/d\n" +
                "4b21593f3f04b3b118e37d5e68927ff/thumb/w%3D400%2Ch%3D600%2Cq%3D90/afisha_event_photo/c5/\n" +
                "f6/be/c5f6be893456d1b70f66d074d0a8f4db.jpg\"/></a><a class=\"btn btn--xs btn--theme-2 t\n" +
                "ext-uppercase\" href=\"https://afisha.relax.by/kino/11117297-tri-bogatyrya-ni-dnya-bez-p\n" +
                "odviga/minsk/\">Test</a><span class=\"b-afisha-layout_maldives_strap_date\">Test\n" +
                "</span><a href=\"https://afisha.relax.by/kino/11117297-tri-bogatyrya-ni-dnya-bez-podviga\n" +
                "/minsk/\" class=\"b-afisha_blocks-strap_item_lnk_txt link\">Test</a></div></div><div class=" +
                "\"b-afisha-layout_strap--item\"><div class=\"b-afisha_bloc\n" +
                "ks-strap_item_lnk_img\"><a href=\"https://afisha.relax.by/kino/11116598-plohije-parni-do-\n" +
                "konca/minsk/\" data-id=\"11116598\" data-name=\"Test\" data-schema-title=\"Test\" data-index=" +
                "\"2\" data-position=\"event_main_page\" class=\"b-afisha_blocks-strap\n" +
                "_item_lnk\"><i class=\"b-afisha_blocks-movies_item_lnk_premier\">Test</i><img height=\"\n" +
                "200\" class=\"b-afisha_blocks-movies_item_img \" src=\"https://ms1.relax.by/images/d4b21593\n" +
                "f3f04b3b118e37d5e68927ff/thumb/w%3D400%2Ch%3D600%2Cq%3D90/afisha_event_photo/99/11/3c/99113\n" +
                "cd14bb0c64cee96995af76746ea.jpg\"/></a><a class=\"btn btn--xs btn--theme-2 text-uppercase\" " +
                "href=\"https://afisha.relax.by/kino/11116598-plohije-parni-do-konca/minsk/\">Test</a><sp an" +
                " class=\"b-afisha-layout_maldives_strap_date\">Test</span><a href=\"https://afisha.r\n" +
                "elax.by/kino/11116598-plohije-parni-do-konca/minsk/\" class=\"b-afisha_blocks-strap_item_ln\n" +
                "k_txt link\">Test</a></div></div></body></html>";
    }
}
