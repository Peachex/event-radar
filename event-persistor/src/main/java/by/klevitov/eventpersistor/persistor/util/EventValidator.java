package by.klevitov.eventpersistor.persistor.util;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.exception.EventValidatorException;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import lombok.extern.log4j.Log4j2;

import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_EVENT;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_OR_EMPTY_EVENT_CATEGORY;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_OR_EMPTY_EVENT_DATE_STR;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_OR_EMPTY_EVENT_ID;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_OR_EMPTY_EVENT_SOURCE_TYPE;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_OR_EMPTY_EVENT_TITLE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
public final class EventValidator {
    private EventValidator() {
    }

    public static void validateEventBeforeCreation(final AbstractEvent event) {
        throwExceptionInCaseOfNullOrEmptyEvent(event);
        throwExceptionInCaseOfEmptyTitle(event.getTitle());
        throwExceptionInCaseOfEmptyDateStr(event.getDateStr());
        throwExceptionInCaseOfEmptyCategory(event.getCategory());
        throwExceptionInCaseOfEmptySourceType(event.getSourceType());
        event.setId(null);
    }

    private static void throwExceptionInCaseOfNullOrEmptyEvent(final AbstractEvent event) {
        if (event == null) {
            log.error(NULL_EVENT);
            throw new EventValidatorException(NULL_EVENT);
        }
    }

    private static void throwExceptionInCaseOfEmptyTitle(final String title) {
        if (isEmpty(title)) {
            log.error(NULL_OR_EMPTY_EVENT_TITLE);
            throw new EventValidatorException(NULL_OR_EMPTY_EVENT_TITLE);
        }
    }

    private static void throwExceptionInCaseOfEmptyDateStr(final String dateStr) {
        if (isEmpty(dateStr)) {
            log.error(NULL_OR_EMPTY_EVENT_DATE_STR);
            throw new EventValidatorException(NULL_OR_EMPTY_EVENT_DATE_STR);
        }
    }

    private static void throwExceptionInCaseOfEmptyCategory(final String category) {
        if (isEmpty(category)) {
            log.error(NULL_OR_EMPTY_EVENT_CATEGORY);
            throw new EventValidatorException(NULL_OR_EMPTY_EVENT_CATEGORY);
        }
    }

    private static void throwExceptionInCaseOfEmptySourceType(final EventSourceType sourceType) {
        if (sourceType == null) {
            log.error(NULL_OR_EMPTY_EVENT_SOURCE_TYPE);
            throw new EventValidatorException(NULL_OR_EMPTY_EVENT_SOURCE_TYPE);
        }
    }

    public static void throwExceptionInCaseOfEmptyId(final String id) {
        if (isEmpty(id)) {
            log.error(NULL_OR_EMPTY_EVENT_ID);
            throw new EventValidatorException(NULL_OR_EMPTY_EVENT_ID);
        }
    }

    public static void validateEventBeforeUpdating(final AbstractEvent event) {
        throwExceptionInCaseOfNullOrEmptyEvent(event);
        throwExceptionInCaseOfEmptyId(event.getId());
    }
}
