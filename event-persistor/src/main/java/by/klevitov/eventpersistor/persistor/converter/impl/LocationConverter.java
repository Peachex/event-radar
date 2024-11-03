package by.klevitov.eventpersistor.persistor.converter.impl;

import by.klevitov.eventpersistor.persistor.converter.EntityConverter;
import by.klevitov.eventpersistor.persistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.springframework.stereotype.Component;

import static by.klevitov.eventpersistor.persistor.util.EntityConverterUtil.throwExceptionInCaseOfNullDTO;
import static by.klevitov.eventpersistor.persistor.util.EntityConverterUtil.throwExceptionInCaseOfNullEntity;

@Component
public class LocationConverter implements EntityConverter {
    @Override
    public AbstractEntity convertFromDTO(final AbstractDTO dto) {
        throwExceptionInCaseOfNullDTO(dto);
        String id = ((LocationDTO) dto).getId();
        String country = ((LocationDTO) dto).getCountry();
        String city = ((LocationDTO) dto).getCity();
        return new Location(id, country, city);
    }

    @Override
    public AbstractDTO convertToDTO(final AbstractEntity entity) {
        throwExceptionInCaseOfNullEntity(entity);
        String id = ((Location) entity).getId();
        String country = ((Location) entity).getCountry();
        String city = ((Location) entity).getCity();
        return new LocationDTO(id, country, city);
    }
}
