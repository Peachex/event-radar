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
        throwExceptionInCaseOfNullLocation(location);
        location.updateRawAddressAndNameWithDefaultsIfNull();
        location.setId(null);
    }

    private static void throwExceptionInCaseOfNullLocation(final Location location) {
        if (location == null) {
            log.error(PersistorExceptionMessage.NULL_LOCATION);
            throw new LocationValidatorException(PersistorExceptionMessage.NULL_LOCATION);
        }
    }

    public static void validateLocationBeforeUpdating(final Location location) {
        throwExceptionInCaseOfNullLocation(location);
        throwExceptionInCaseOfEmptyId(location.getId());
        throwExceptionInCaseOfEmptyRawAddressAndName(location.getRawAddress(), location.getName());
    }

    public static void throwExceptionInCaseOfEmptyId(final String id) {
        if (isEmpty(id)) {
            log.error(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_ID);
            throw new LocationValidatorException(PersistorExceptionMessage.NULL_OR_EMPTY_LOCATION_ID);
        }
    }

    private static void throwExceptionInCaseOfEmptyRawAddressAndName(final String rawAddress, final String name) {
        if (isEmpty(rawAddress) || isEmpty(name)) {
            log.error(PersistorExceptionMessage.NULL_OR_EMPTY_RAW_ADDRESSOR_LOCATION_NAME);
            throw new LocationValidatorException(PersistorExceptionMessage.NULL_OR_EMPTY_RAW_ADDRESSOR_LOCATION_NAME);
        }
    }
}
