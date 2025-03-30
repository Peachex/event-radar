package by.klevitov.eventmanager.service.impl;

import by.klevitov.eventmanager.service.EventFetcherService;
import by.klevitov.eventmanager.service.impl.EventFetcherServiceImpl;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static by.klevitov.eventradarcommon.dto.EventSourceType.AFISHA_RELAX;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EventFetcherServiceImplTest {
    private EventFetcherService eventFetcherService;
    private EventParserService mockedParserService;

    @BeforeEach
    public void setUp() {
        mockedParserService = Mockito.mock(EventParserService.class);
        eventFetcherService = new EventFetcherServiceImpl(mockedParserService);
    }

    @Test
    public void test_fetch() throws Exception {
        when(mockedParserService.retrieveAvailableParsers())
                .thenReturn(Map.of(AFISHA_RELAX, Mockito.mock(EventParser.class)));
        when(mockedParserService.retrieveEvents(any()))
                .thenReturn(List.of(
                        AfishaRelaxEventDTO.builder().title("title").sourceType(AFISHA_RELAX).build(),
                        ByCardEventDTO.builder().title("title").sourceType(BYCARD).build())
                );
        List<AbstractEventDTO> expected = List.of(
                AfishaRelaxEventDTO.builder().title("title").sourceType(AFISHA_RELAX).build(),
                ByCardEventDTO.builder().title("title").sourceType(BYCARD).build()
        );
        List<AbstractEventDTO> actual = eventFetcherService.fetch();
        assertEquals(expected, actual);
    }
}
