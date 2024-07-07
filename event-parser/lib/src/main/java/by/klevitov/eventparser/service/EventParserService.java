package by.klevitov.eventparser.service;

import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;

import java.util.List;
import java.util.Map;

public interface EventParserService {
    Map<EventSourceType, EventParser> retrieveAvailableParsers();

    List<AbstractEventDTO> retrieveEvents(final EventParser parser) throws EventParserServiceException;
}
