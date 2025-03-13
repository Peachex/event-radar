package by.klevitov.eventpersistor.common.util;

import by.klevitov.eventpersistor.common.dto.PageRequestDTO;
import by.klevitov.eventpersistor.common.dto.PageRequestDTO.SortField;
import by.klevitov.eventpersistor.exception.PageRequestValidatorException;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;

import java.util.List;

import static by.klevitov.eventpersistor.constant.PersistorExceptionMessage.INVALID_PAGE_NUMBER;
import static by.klevitov.eventpersistor.constant.PersistorExceptionMessage.INVALID_PAGE_SIZE;
import static by.klevitov.eventpersistor.constant.PersistorExceptionMessage.INVALID_SORT_DIRECTION;
import static by.klevitov.eventpersistor.constant.PersistorExceptionMessage.NULL_PAGE_REQUEST;
import static by.klevitov.eventpersistor.constant.PersistorExceptionMessage.NULL_SORT_FIELD;
import static by.klevitov.eventpersistor.constant.PersistorExceptionMessage.NULL_SORT_FIELD_DIRECTION;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
public final class PageRequestValidator {
    private PageRequestValidator() {
    }

    public static void validatePageRequest(final PageRequestDTO pageRequest) {
        throwExceptionInCaseOfNullOrEmptyPageRequest(pageRequest);
        throwExceptionInCaseOfInvalidPageNumber(pageRequest.getPage());
        throwExceptionInCaseOfInvalidPageSize(pageRequest.getSize());
        throwExceptionInCaseOfInvalidSortFields(pageRequest.getSortFields());
    }

    private static void throwExceptionInCaseOfNullOrEmptyPageRequest(final PageRequestDTO pageRequest) throws PageRequestValidatorException {
        if (pageRequest == null) {
            log.error(NULL_PAGE_REQUEST);
            throw new PageRequestValidatorException(NULL_PAGE_REQUEST);
        }
    }

    private static void throwExceptionInCaseOfInvalidPageNumber(final int page) {
        if (page < 0) {
            final String exceptionMessage = String.format(INVALID_PAGE_NUMBER, page);
            log.error(exceptionMessage);
            throw new PageRequestValidatorException(exceptionMessage);
        }
    }

    private static void throwExceptionInCaseOfInvalidPageSize(final int size) {
        if (size <= 0) {
            final String exceptionMessage = String.format(INVALID_PAGE_SIZE, size);
            log.error(exceptionMessage);
            throw new PageRequestValidatorException(exceptionMessage);
        }
    }

    private static void throwExceptionInCaseOfInvalidSortFields(final List<SortField> sortFields) {
        if (isNotEmpty(sortFields)) {
            for (SortField sortField : sortFields) {
                throwExceptionInCaseOfNullOrEmptySortField(sortField.getField());
                throwExceptionInCaseOfNullOrEmptySortFieldDirection(sortField.getDirection());
                throwExceptionInCaseOfInvalidSortFieldDirection(sortField.getDirection());
            }
        }
    }

    private static void throwExceptionInCaseOfNullOrEmptySortField(final String sortField) {
        if (isEmpty(sortField)) {
            log.error(NULL_SORT_FIELD);
            throw new PageRequestValidatorException(NULL_SORT_FIELD);
        }
    }

    private static void throwExceptionInCaseOfNullOrEmptySortFieldDirection(final String direction) {
        if (isEmpty(direction)) {
            log.error(NULL_SORT_FIELD_DIRECTION);
            throw new PageRequestValidatorException(NULL_SORT_FIELD_DIRECTION);
        }
    }

    private static void throwExceptionInCaseOfInvalidSortFieldDirection(final String direction) {
        try {
            Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            final String exceptionMessage = String.format(INVALID_SORT_DIRECTION, direction);
            log.error(exceptionMessage);
            throw new PageRequestValidatorException(exceptionMessage);
        }
    }
}
