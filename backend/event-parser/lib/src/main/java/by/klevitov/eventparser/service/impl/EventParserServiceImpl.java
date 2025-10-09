package by.klevitov.eventparser.service.impl;

import by.klevitov.eventparser.configuration.EventParserConfiguration;
import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.exception.HTMLDocumentRetrievingException;
import by.klevitov.eventparser.exception.InvalidParserException;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

import static by.klevitov.eventparser.configuration.EventParserConfiguration.parserIsUnknown;
import static by.klevitov.eventparser.constant.ExceptionMessage.ERROR_INTERRUPTED_THREAD_DURING_DELAY;
import static by.klevitov.eventparser.constant.ExceptionMessage.ERROR_RETRIEVING_EVENTS_DTO;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_PARSER;
import static by.klevitov.eventparser.constant.ExceptionMessage.UNKNOWN_PARSER;
import static by.klevitov.eventparser.util.JsoupUtil.retrieveDocumentByURL;

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
            List<AbstractEventDTO> events = parser.parse(htmlDocument);
            updateEventsWithLocation(events, parser);
            return events;
        } catch (HTMLDocumentRetrievingException e) {
            log.error(String.format(ERROR_RETRIEVING_EVENTS_DTO, e.getMessage()));
            throw new EventParserServiceException(e);
        }
    }

    private void throwExceptionInCaseOfEmptyOrUnknownParser(final EventParser parser) {
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

    private void updateEventsWithLocation(final List<AbstractEventDTO> events, final EventParser parser)
            throws HTMLDocumentRetrievingException, EventParserServiceException {
        if (CollectionUtils.isEmpty(events)) {
            return;
        }

        for (int i = 0; i < events.size(); i++) {
            AbstractEventDTO event = events.get(i);
            if (event.getSourceType() == null) {
                continue;
            }

            String eventLink = extractEventLink(event);
            if (StringUtils.isBlank(eventLink)) {
                continue;
            }

            Document eventDocument = retrieveDocumentByURL(eventLink);
            event.setLocation(parser.parseLocation(eventDocument));
            if (i < events.size() - 1) {
                pauseBetweenRequests();
            }
        }
    }

    private String extractEventLink(final AbstractEventDTO event) {
        return switch (event.getSourceType()) {
            case AFISHA_RELAX -> ((AfishaRelaxEventDTO) event).getEventLink();
            case BYCARD -> ((ByCardEventDTO) event).getEventLink();
        };
    }

    private void pauseBetweenRequests() throws EventParserServiceException {
        try {
            Thread.sleep(EventParserConfiguration.getRequestsDelayMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(String.format(ERROR_INTERRUPTED_THREAD_DURING_DELAY, e.getMessage()));
            throw new EventParserServiceException(e);
        }
    }
}
