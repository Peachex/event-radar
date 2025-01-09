package by.klevitov.eventpersistor.factory;

import by.klevitov.eventpersistor.constant.PersistorExceptionMessage;
import by.klevitov.eventpersistor.converter.EntityConverter;
import by.klevitov.eventpersistor.converter.impl.AfishaRelaxEventConverter;
import by.klevitov.eventpersistor.converter.impl.ByCardEventConverter;
import by.klevitov.eventpersistor.converter.impl.LocationConverter;
import by.klevitov.eventpersistor.exception.EntityConverterFactoryException;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.klevitov.eventradarcommon.dto.EventSourceType.AFISHA_RELAX;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;

@Log4j2
@Component
public class EntityConverterFactory {
    private final Map<Class<? extends AbstractDTO>, EntityConverter> convertersMapWithClassKey;
    private final Map<EventSourceType, EntityConverter> convertersMapWithEventSourceTypeKey;

    @Autowired
    public EntityConverterFactory(List<EntityConverter> converters) {
        this.convertersMapWithClassKey = new HashMap<>();
        this.convertersMapWithEventSourceTypeKey = new HashMap<>();
        for (EntityConverter converter : converters) {
            if (converter instanceof AfishaRelaxEventConverter) {
                convertersMapWithClassKey.put(AfishaRelaxEventDTO.class, converter);
                convertersMapWithEventSourceTypeKey.put(AFISHA_RELAX, converter);
            } else if (converter instanceof ByCardEventConverter) {
                convertersMapWithClassKey.put(ByCardEventDTO.class, converter);
                convertersMapWithEventSourceTypeKey.put(BYCARD, converter);
            } else if (converter instanceof LocationConverter) {
                convertersMapWithClassKey.put(LocationDTO.class, converter);
            }
        }
    }

    public EntityConverter getConverter(final Class<? extends AbstractDTO> dtoClass) {
        EntityConverter converter = convertersMapWithClassKey.get(dtoClass);
        throwExceptionInCaseOfConverterNotFound(converter, dtoClass);
        return converter;
    }

    private void throwExceptionInCaseOfConverterNotFound(final EntityConverter converter,
                                                         final Class<? extends AbstractDTO> dtoClass) {
        if (converter == null) {
            final String exceptionMessage = String.format(PersistorExceptionMessage.ENTITY_CONVERTER_NOT_FOUND, dtoClass);
            log.error(exceptionMessage);
            throw new EntityConverterFactoryException(exceptionMessage);
        }
    }

    public EntityConverter getConverter(final EventSourceType sourceType) {
        EntityConverter converter = convertersMapWithEventSourceTypeKey.get(sourceType);
        throwExceptionInCaseOfConverterNotFound(converter, sourceType);
        return converter;
    }

    private void throwExceptionInCaseOfConverterNotFound(final EntityConverter converter,
                                                         final EventSourceType sourceType) {
        if (converter == null) {
            final String exceptionMessage = String.format(PersistorExceptionMessage.ENTITY_CONVERTER_NOT_FOUND, sourceType);
            log.error(exceptionMessage);
            throw new EntityConverterFactoryException(exceptionMessage);
        }
    }
}
