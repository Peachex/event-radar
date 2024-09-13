package by.klevitov.eventpersistor.messaging.factory;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.converter.impl.AfishaRelaxEventConverter;
import by.klevitov.eventpersistor.messaging.converter.impl.LocationConverter;
import by.klevitov.eventpersistor.messaging.exception.EntityConverterFactoryException;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.ENTITY_CONVERTER_NOT_FOUND;

@Log4j2
@Component
public class EntityConverterFactory {
    private final Map<Class<? extends AbstractDTO>, EntityConverter> convertersMap;

    @Autowired
    public EntityConverterFactory(List<EntityConverter> converters) {
        this.convertersMap = new HashMap<>();
        for (EntityConverter converter : converters) {
            if (converter instanceof AfishaRelaxEventConverter) {
                convertersMap.put(AfishaRelaxEventDTO.class, converter);
            } else if (converter instanceof ByCardEventDTO) {
                convertersMap.put(ByCardEventDTO.class, converter);
            } else if (converter instanceof LocationConverter) {
                convertersMap.put(LocationDTO.class, converter);

            }
        }
    }

    public EntityConverter getConverter(final Class<? extends AbstractDTO> dtoClass) {
        EntityConverter converter = convertersMap.get(dtoClass);
        throwExceptionInCaseOfConverterNotFound(converter, dtoClass);
        return converter;
    }

    private void throwExceptionInCaseOfConverterNotFound(final EntityConverter converter,
                                                         final Class<? extends AbstractDTO> dtoClass) {
        if (converter == null) {
            final String exceptionMessage = String.format(ENTITY_CONVERTER_NOT_FOUND, dtoClass);
            log.error(exceptionMessage);
            throw new EntityConverterFactoryException(exceptionMessage);
        }
    }
}
