package by.klevitov.eventparser.service;

import by.klevitov.eventparser.dto.AbstractEventDTO;
import by.klevitov.eventparser.dto.EventSourceType;
import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.parser.EventParser;

import java.util.List;
import java.util.Map;

public interface EventParserService {
    Map<EventSourceType, EventParser> retrieveAvailableParsers() throws EventParserServiceException;

    List<AbstractEventDTO> retrieveEvents(final EventParser parser) throws EventParserServiceException;
}
