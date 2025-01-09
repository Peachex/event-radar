package by.klevitov.eventpersistor.service.impl;

import by.klevitov.eventpersistor.converter.EntityConverter;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.service.EntityConverterService;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationConverterService implements EntityConverterService<Location, LocationDTO> {
    private final EntityConverter converter;

    @Autowired
    public LocationConverterService(EntityConverterFactory converterFactory) {
        converter = converterFactory.getConverter(LocationDTO.class);
    }

    @Override
    public Location convertFromDTO(final LocationDTO dto) {
        return (Location) converter.convertFromDTO(dto);
    }

    @Override
    public List<Location> convertFromDTO(final List<LocationDTO> dto) {
        return dto.stream().map(l -> (Location) converter.convertFromDTO(l)).toList();
    }

    @Override
    public LocationDTO convertToDTO(final Location entity) {
        return (LocationDTO) converter.convertToDTO(entity);
    }

    @Override
    public List<LocationDTO> convertToDTO(final List<Location> entities) {
        return entities.stream().map(l -> (LocationDTO) converter.convertToDTO(l)).toList();
    }
}
