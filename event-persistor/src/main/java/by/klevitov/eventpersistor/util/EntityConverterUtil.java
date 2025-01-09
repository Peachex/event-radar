package by.klevitov.eventpersistor.util;

import by.klevitov.eventpersistor.constant.PersistorExceptionMessage;
import by.klevitov.eventpersistor.converter.impl.LocationConverter;
import by.klevitov.eventpersistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.exception.EntityConverterException;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class EntityConverterUtil {
    private EntityConverterUtil() {
    }

    public static Location extractLocationFromEventDTO(final AbstractEventDTO eventDTO,
                                                       final LocationConverter converter) {
        LocationDTO locationDTO = eventDTO.getLocation();
        return (locationDTO != null ? (Location) converter.convertFromDTO(locationDTO) : new Location());
    }

    public static void throwExceptionInCaseOfNullDTO(final AbstractDTO dto) {
        if (dto == null) {
            final String exceptionMessage = PersistorExceptionMessage.NULL_DTO;
            log.error(exceptionMessage);
            throw new EntityConverterException(exceptionMessage);
        }
    }

    public static void throwExceptionInCaseOfNullEntity(final AbstractEntity entity) {
        if (entity == null) {
            final String exceptionMessage = PersistorExceptionMessage.NULL_ENTITY;
            log.error(exceptionMessage);
            throw new EntityConverterException(exceptionMessage);
        }
    }
}
