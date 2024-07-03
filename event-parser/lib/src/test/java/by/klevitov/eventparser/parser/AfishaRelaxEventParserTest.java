package by.klevitov.eventparser.parser;

import by.klevitov.eventparser.creator.EventCreator;
import by.klevitov.eventparser.dto.AbstractEventDTO;
import by.klevitov.eventparser.dto.AfishaRelaxEventDTO;
import by.klevitov.eventparser.exception.HTMLDocumentRetrievingException;
import by.klevitov.eventparser.parser.impl.AfishaRelaxEventParser;
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
import static by.klevitov.eventparser.constant.EventField.SOURCE_TYPE;
import static by.klevitov.eventparser.constant.EventField.TITLE;
import static by.klevitov.eventparser.constant.EventLocation.BELARUS;
import static by.klevitov.eventparser.constant.EventLocation.MINSK;
import static by.klevitov.eventparser.dto.EventSourceType.AFISHA_RELAX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class AfishaRelaxEventParserTest {
    private static EventParser parser;
    @Mock
    private static EventCreator creator;

    @BeforeAll
    public static void init() {
        creator = Mockito.mock();
        parser = new AfishaRelaxEventParser(creator);
    }

    @Test
    public void test_parse() throws HTMLDocumentRetrievingException {
        Mockito.when(creator.create(Mockito.anyMap())).thenReturn(new AfishaRelaxEventDTO());
        Document htmlDocument = JsoupUtil.retrieveDocumentByURL("https://afisha.relax.by");
        List<AbstractEventDTO> events = parser.parse(htmlDocument);
        assertTrue(isNotEmpty(events));
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
}
