package by.klevitov.eventmanager.service.impl;

import by.klevitov.eventmanager.service.EventFetcherService;
import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventFetcherServiceImpl implements EventFetcherService {
    private final EventParserService eventParserService;

    @Autowired
    public EventFetcherServiceImpl(EventParserService eventParserService) {
        this.eventParserService = eventParserService;
    }

    @Override
    public List<AbstractEventDTO> fetch() throws EventParserServiceException {
        final List<AbstractEventDTO> events = new ArrayList<>();
        for (EventParser parser : eventParserService.retrieveAvailableParsers().values()) {
            events.addAll(eventParserService.retrieveEvents(parser));
        }
        return events;
    }
}
