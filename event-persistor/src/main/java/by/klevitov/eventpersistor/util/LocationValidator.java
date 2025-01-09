package by.klevitov.eventpersistor.util;

import by.klevitov.eventpersistor.constant.PersistorExceptionMessage;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.exception.LocationValidatorException;
import lombok.extern.log4j.Log4j2;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
public final class LocationValidator {
    private LocationValidator() {
    }

    public static void validateLocationBeforeCreation(final Location location) {
        throwExceptionInCaseOfNullOrEmptyLocation(location);
        throwExceptionInCaseOfEmptyCountry(location.getCountry());
        throwExceptionInCaseOfEmptyCity(location.getCity());
        location.setId(null);
    }

    private static void throwExceptionInCaseOfNullOrEmptyLocation(final Location location) {
        if (location == null) {
            log.error(PersistorExceptionMessage.NULL_LOCATION);
            throw new LocationValidatorException(PersistorExceptionMessage.NULL_LOCATION);
        }
    }

    private static void throwExceptionInCaseOfEmptyCountry(final String country) {
        if (isEmpty(country)) {
            log.error(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_COUNTRY);
            throw new LocationValidatorException(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_COUNTRY);
        }
    }

    private static void throwExceptionInCaseOfEmptyCity(final String city) {
        if (isEmpty(city)) {
            log.error(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_CITY);
            throw new LocationValidatorException(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_CITY);
        }
    }

    public static void validateLocationBeforeUpdating(final Location location) {
        throwExceptionInCaseOfNullOrEmptyLocation(location);
        throwExceptionInCaseOfEmptyId(location.getId());
        throwExceptionInCaseOfEmptyCountryAndCity(location.getCountry(), location.getCity());
    }

    public static void throwExceptionInCaseOfEmptyId(final String id) {
        if (isEmpty(id)) {
            log.error(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_ID);
            throw new LocationValidatorException(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_ID);
        }
    }

    private static void throwExceptionInCaseOfEmptyCountryAndCity(final String country, final String city) {
        if (isEmpty(country) && isEmpty(city)) {
            log.error(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_COUNTRY_OR_CITY);
            throw new LocationValidatorException(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_COUNTRY_OR_CITY);
        }
    }
}
