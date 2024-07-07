package by.klevitov.eventparser.service.impl;

import by.klevitov.eventparser.configuration.EventParserConfiguration;
import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.exception.HTMLDocumentRetrievingException;
import by.klevitov.eventparser.exception.InvalidParserException;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_PARSER;
import static by.klevitov.eventparser.constant.ExceptionMessage.UNKNOWN_PARSER;
import static by.klevitov.eventparser.util.JsoupUtil.retrieveDocumentByURL;
import static by.klevitov.eventparser.constant.ExceptionMessage.ERROR_RETRIEVING_EVENTS_DTO;
import static by.klevitov.eventparser.configuration.EventParserConfiguration.parserIsUnknown;

@Log4j2
public class EventParserServiceImpl implements EventParserService {
    @Override
    public Map<EventSourceType, EventParser> retrieveAvailableParsers() {
        return EventParserConfiguration.getAvailableParsers();
    }

    @Override
    public List<AbstractEventDTO> retrieveEvents(final EventParser parser) throws EventParserServiceException {
        try {
            throwExceptionInCaseOfEmptyOrUnknownParser(parser);
            final String siteURL = parser.retrieveSiteURL();
            Document htmlDocument = retrieveDocumentByURL(siteURL);
            return parser.parse(htmlDocument);
        } catch (HTMLDocumentRetrievingException e) {
            log.error(String.format(ERROR_RETRIEVING_EVENTS_DTO, e.getMessage()));
            throw new EventParserServiceException(e);
        }
    }

    private static void throwExceptionInCaseOfEmptyOrUnknownParser(final EventParser parser) {
        if (parser == null) {
            log.error(NULL_PARSER);
            throw new InvalidParserException(NULL_PARSER);
        }

        if (parserIsUnknown(parser)) {
            String errorMessage = String.format(UNKNOWN_PARSER, parser.retrieveSiteURL());
            log.error(errorMessage);
            throw new InvalidParserException(errorMessage);
        }
    }
}
