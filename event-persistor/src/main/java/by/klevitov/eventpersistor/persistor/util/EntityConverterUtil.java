package by.klevitov.eventpersistor.persistor.util;

import by.klevitov.eventpersistor.persistor.exception.EntityConverterException;
import by.klevitov.eventpersistor.persistor.entity.AbstractEntity;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import lombok.extern.log4j.Log4j2;

import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_DTO;
import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.NULL_ENTITY;

@Log4j2
public final class EntityConverterUtil {
    private EntityConverterUtil() {
    }

    public static void throwExceptionInCaseOfNullDTO(final AbstractDTO dto) {
        if (dto == null) {
            final String exceptionMessage = NULL_DTO;
            log.error(exceptionMessage);
            throw new EntityConverterException(exceptionMessage);
        }
    }

    public static void throwExceptionInCaseOfNullEntity(final AbstractEntity entity) {
        if (entity == null) {
            final String exceptionMessage = NULL_ENTITY;
            log.error(exceptionMessage);
            throw new EntityConverterException(exceptionMessage);
        }
    }
}
