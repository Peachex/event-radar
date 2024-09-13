package by.klevitov.eventpersistor.messaging.config;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.converter.impl.AfishaRelaxEventConverter;
import by.klevitov.eventpersistor.messaging.converter.impl.ByCardEventConverter;
import by.klevitov.eventpersistor.messaging.converter.impl.LocationConverter;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class EntityConverterConfig {
    @Bean
    public Map<Class<? extends AbstractDTO>, EntityConverter> convertersMap(
            AfishaRelaxEventConverter afishaRelaxEventConverter, ByCardEventConverter byCardEventConverter,
            LocationConverter locationConverter) {
        return Map.of(
                AfishaRelaxEventDTO.class, afishaRelaxEventConverter,
                ByCardEventDTO.class, byCardEventConverter,
                LocationDTO.class, locationConverter
        );
    }

    @Bean
    public EntityConverterFactory converterFactory(Map<Class<? extends AbstractDTO>, EntityConverter> convertersMap) {
        return new EntityConverterFactory(convertersMap);
    }
}
