package by.klevitov.eventpersistor.messaging.factory;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.EntityConverterFactoryException;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.ENTITY_CONVERTER_NOT_FOUND;

@Log4j2
@Component
public class EntityConverterFactory {
    private final Map<Class<? extends AbstractDTO>, EntityConverter> converters;

    @Autowired
    public EntityConverterFactory(Map<Class<? extends AbstractDTO>, EntityConverter> converters) {
        this.converters = converters;
    }

    public EntityConverter getConverter(final Class<? extends AbstractDTO> dtoClass) {
        EntityConverter converter = converters.get(dtoClass);
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
