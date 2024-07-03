package by.klevitov.eventparser;

import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventparser.service.impl.EventParserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) throws EventParserServiceException {
        EventParserService parserService = new EventParserServiceImpl();
        List<EventParser> parsers = parserService.retrieveAvailableParsers();
        parsers.forEach(p -> System.out.println(p.retrieveSiteURL()));
    }

    //todo Delete this class.
}
