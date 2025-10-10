package by.klevitov.eventpersistor.converter.impl;

import by.klevitov.eventpersistor.converter.EntityConverter;
import by.klevitov.eventpersistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.springframework.stereotype.Component;

import static by.klevitov.eventpersistor.util.EntityConverterUtil.throwExceptionInCaseOfNullDTO;
import static by.klevitov.eventpersistor.util.EntityConverterUtil.throwExceptionInCaseOfNullEntity;

@Component
public class LocationConverter implements EntityConverter {
    @Override
    public AbstractEntity convertFromDTO(final AbstractDTO dto) {
        throwExceptionInCaseOfNullDTO(dto);
        String id = ((LocationDTO) dto).getId();
        String name = ((LocationDTO) dto).getName();
        String country = ((LocationDTO) dto).getCountry();
        String city = ((LocationDTO) dto).getCity();
        String rawAddress = ((LocationDTO) dto).getRawAddress();
        double latitude = ((LocationDTO) dto).getLatitude();
        double longitude = ((LocationDTO) dto).getLongitude();
        return new Location(id, name, country, city, rawAddress, latitude, longitude);
    }

    @Override
    public AbstractDTO convertToDTO(final AbstractEntity entity) {
        throwExceptionInCaseOfNullEntity(entity);
        String id = ((Location) entity).getId();
        String name = ((Location) entity).getName();
        String country = ((Location) entity).getCountry();
        String city = ((Location) entity).getCity();
        String rawAddress = ((Location) entity).getRawAddress();
        double latitude = ((Location) entity).getLatitude();
        double longitude = ((Location) entity).getLongitude();
        return new LocationDTO(id, name, country, city, rawAddress, latitude, longitude);
    }
}
