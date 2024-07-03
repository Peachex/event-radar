package by.klevitov.eventparser;

import by.klevitov.eventparser.dto.EventSourceType;
import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventparser.service.impl.EventParserServiceImpl;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws EventParserServiceException {
        EventParserService parserService = new EventParserServiceImpl();
        Map<EventSourceType, EventParser> parsers = parserService.retrieveAvailableParsers();
        EventParser parser = parsers.get(EventSourceType.AFISHA_RELAX);
        if (parser != null) {
            System.out.println(parserService.retrieveEvents(parser));
        }
    }

    //todo Delete this class.
}
