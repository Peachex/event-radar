package by.klevitov.eventpersistor.messaging.converter.impl;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.persistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.springframework.stereotype.Component;

import static by.klevitov.eventpersistor.messaging.util.EntityConverterUtil.throwExceptionInCaseOfNullDTO;
import static by.klevitov.eventpersistor.messaging.util.EntityConverterUtil.throwExceptionInCaseOfNullEntity;

@Component
public class LocationConverter implements EntityConverter {
    @Override
    public AbstractEntity convertFromDTO(final AbstractDTO dto) {
        throwExceptionInCaseOfNullDTO(dto);
        String country = ((LocationDTO) dto).getCountry();
        String city = ((LocationDTO) dto).getCity();
        return new Location(country, city);
    }

    @Override
    public AbstractDTO convertToDTO(final AbstractEntity entity) {
        throwExceptionInCaseOfNullEntity(entity);
        String country = ((Location) entity).getCountry();
        String city = ((Location) entity).getCity();
        return new LocationDTO(country, city);
    }
}
