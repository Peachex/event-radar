package by.klevitov.eventparser.service;

import by.klevitov.eventparser.configuration.EventParserConfiguration;
import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.exception.HTMLDocumentRetrievingException;
import by.klevitov.eventparser.exception.InvalidParserException;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.impl.EventParserServiceImpl;
import by.klevitov.eventparser.util.JsoupUtil;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EventParserServiceImplTest {
    @Mock
    private EventParser parser;
    private static EventParserService service;

    @BeforeAll
    public static void init() {
        service = new EventParserServiceImpl();
    }

    @BeforeEach
    public void reset() {
        Mockito.clearAllCaches();
        parser = Mockito.mock();
    }

    @Test
    public void test_retrieveAvailableParsers() {
        Map<EventSourceType, EventParser> expected = EventParserConfiguration.getAvailableParsers();
        Map<EventSourceType, EventParser> actual = service.retrieveAvailableParsers();
        assertEquals(expected, actual);
    }

    @Test
    public void test_retrieveEvents_withValidParser() throws EventParserServiceException {
        try (MockedStatic<JsoupUtil> jsoupUtil = Mockito.mockStatic(JsoupUtil.class);
             MockedStatic<EventParserConfiguration> parserConfiguration = Mockito.mockStatic(
                     EventParserConfiguration.class)) {
            jsoupUtil.when(() -> JsoupUtil.retrieveDocumentByURL(Mockito.anyString()))
                    .thenReturn(new Document("baseUri"));

            parserConfiguration.when(() -> EventParserConfiguration.parserIsUnknown(Mockito.any(EventParser.class)))
                    .thenReturn(false);

            Mockito.when(parser.retrieveSiteURL())
                    .thenReturn("siteURL");

            Mockito.when(parser.parse(Mockito.any(Document.class)))
                    .thenReturn(List.of(new AfishaRelaxEventDTO(), new ByCardEventDTO()));

            List<AbstractEventDTO> expected = List.of(new AfishaRelaxEventDTO(), new ByCardEventDTO());
            List<AbstractEventDTO> actual = service.retrieveEvents(parser);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void test_retrieveEvents_withEmptyParser() {
        EventParser nullParser = null;
        assertThrows(InvalidParserException.class, () -> service.retrieveEvents(nullParser));
    }

    @Test
    public void test_retrieveEvents_withUnknownParser() {
        try (MockedStatic<EventParserConfiguration> parserConfiguration = Mockito.mockStatic(
                EventParserConfiguration.class)) {
            parserConfiguration.when(() -> EventParserConfiguration.parserIsUnknown(Mockito.any(EventParser.class)))
                    .thenReturn(true);
            assertThrows(InvalidParserException.class, () -> service.retrieveEvents(parser));
        }
    }

    @Test
    public void test_retrieveEvents_withValidParserAndInvalidSite() {
        try (MockedStatic<JsoupUtil> jsoupUtil = Mockito.mockStatic(JsoupUtil.class);
             MockedStatic<EventParserConfiguration> parserConfiguration = Mockito.mockStatic(
                     EventParserConfiguration.class)) {
            jsoupUtil.when(() -> JsoupUtil.retrieveDocumentByURL(Mockito.anyString()))
                    .thenThrow(new HTMLDocumentRetrievingException("There was an error during html document"
                            + " retrieving. More details: %s"));

            parserConfiguration.when(() -> EventParserConfiguration.parserIsUnknown(Mockito.any(EventParser.class)))
                    .thenReturn(false);

            Mockito.when(parser.retrieveSiteURL())
                    .thenReturn("siteURL");

            assertThrows(EventParserServiceException.class, () -> service.retrieveEvents(parser));
        }
    }
}
