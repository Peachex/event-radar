package by.klevitov.eventparser.service;

import by.klevitov.eventparser.dto.AbstractEventDTO;
import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.parser.EventParser;

import java.util.List;

public interface EventParserService {
    List<EventParser> retrieveAvailableParsers() throws EventParserServiceException;

    List<AbstractEventDTO> retrieveEvents(final EventParser parser) throws EventParserServiceException;
}
