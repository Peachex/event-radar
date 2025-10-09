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
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static by.klevitov.eventparser.configuration.EventParserConfiguration.getRequestsDelayMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void test_retrieveEvents_shouldSetLocationFromEventPage() throws EventParserServiceException {
        try (MockedStatic<JsoupUtil> jsoupUtil = Mockito.mockStatic(JsoupUtil.class);
             MockedStatic<EventParserConfiguration> parserConfig = Mockito.mockStatic(EventParserConfiguration.class)) {
            Document dummyDoc = new Document("baseUri");

            jsoupUtil.when(() -> JsoupUtil.retrieveDocumentByURL(Mockito.anyString()))
                    .thenReturn(dummyDoc);
            parserConfig.when(() -> EventParserConfiguration.parserIsUnknown(Mockito.any()))
                    .thenReturn(false);

            AfishaRelaxEventDTO afishaEvent = Mockito.spy(new AfishaRelaxEventDTO());
            afishaEvent.setSourceType(EventSourceType.AFISHA_RELAX);
            afishaEvent.setEventLink("http://event1.com");

            ByCardEventDTO bycardEvent = Mockito.spy(new ByCardEventDTO());
            bycardEvent.setSourceType(EventSourceType.BYCARD);
            bycardEvent.setEventLink("http://event2.com");

            Mockito.when(parser.retrieveSiteURL()).thenReturn("http://site.com");
            Mockito.when(parser.parse(dummyDoc)).thenReturn(List.of(afishaEvent, bycardEvent));

            LocationDTO location1 = LocationDTO.builder().name("Venue A").build();
            LocationDTO location2 = LocationDTO.builder().name("Venue B").build();

            Mockito.when(parser.parseLocation(dummyDoc)).thenReturn(location1).thenReturn(location2);

            List<AbstractEventDTO> events = service.retrieveEvents(parser);

            assertEquals(2, events.size());
            assertEquals("Venue A", events.get(0).getLocation().getName());
            assertEquals("Venue B", events.get(1).getLocation().getName());
        }
    }

    @Test
    public void test_retrieveEvents_shouldSkipEventWithNullSourceTypeOrBlankLink() throws EventParserServiceException {
        try (MockedStatic<JsoupUtil> jsoupUtil = Mockito.mockStatic(JsoupUtil.class);
             MockedStatic<EventParserConfiguration> parserConfig = Mockito.mockStatic(EventParserConfiguration.class)) {
            Document dummyDoc = new Document("baseUri");

            jsoupUtil.when(() -> JsoupUtil.retrieveDocumentByURL(Mockito.anyString()))
                    .thenReturn(dummyDoc);
            parserConfig.when(() -> EventParserConfiguration.parserIsUnknown(Mockito.any()))
                    .thenReturn(false);

            AfishaRelaxEventDTO afishaEvent = new AfishaRelaxEventDTO();
            afishaEvent.setSourceType(null);
            afishaEvent.setEventLink("http://valid.com");

            ByCardEventDTO bycardEvent = new ByCardEventDTO();
            bycardEvent.setSourceType(EventSourceType.BYCARD);
            bycardEvent.setEventLink("   "); // Blank link

            Mockito.when(parser.retrieveSiteURL()).thenReturn("http://site.com");
            Mockito.when(parser.parse(dummyDoc)).thenReturn(List.of(afishaEvent, bycardEvent));

            List<AbstractEventDTO> events = service.retrieveEvents(parser);

            Mockito.verify(parser, Mockito.never()).parseLocation(Mockito.any());
            assertNull(events.get(0).getLocation());
            assertNull(events.get(1).getLocation());
        }
    }

    @Test
    public void test_retrieveEvents_shouldPauseBetweenRequests() throws EventParserServiceException {
        try (MockedStatic<JsoupUtil> jsoupUtil = Mockito.mockStatic(JsoupUtil.class);
             MockedStatic<EventParserConfiguration> config = Mockito.mockStatic(EventParserConfiguration.class)) {
            config.when(EventParserConfiguration::getRequestsDelayMillis).thenReturn(1000L);
            config.when(() -> EventParserConfiguration.parserIsUnknown(Mockito.any())).thenReturn(false);

            Document dummyDoc = new Document("baseUri");
            jsoupUtil.when(() -> JsoupUtil.retrieveDocumentByURL(Mockito.anyString()))
                    .thenReturn(dummyDoc);

            ByCardEventDTO event1 = new ByCardEventDTO();
            event1.setSourceType(EventSourceType.BYCARD);
            event1.setEventLink("http://event1.com");

            ByCardEventDTO event2 = new ByCardEventDTO();
            event2.setSourceType(EventSourceType.BYCARD);
            event2.setEventLink("http://event2.com");

            Mockito.when(parser.retrieveSiteURL()).thenReturn("http://site.com");
            Mockito.when(parser.parse(dummyDoc)).thenReturn(List.of(event1, event2));
            Mockito.when(parser.parseLocation(dummyDoc)).thenReturn(LocationDTO.builder().name("Loc").build());

            long start = System.currentTimeMillis();
            service.retrieveEvents(parser);
            long duration = System.currentTimeMillis() - start;

            assertTrue(duration >= getRequestsDelayMillis());
        }
    }
}
