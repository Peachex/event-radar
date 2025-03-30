package by.klevitov.eventpersistor.converter.impl;

import by.klevitov.eventpersistor.converter.EntityConverter;
import by.klevitov.eventpersistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.entity.AfishaRelaxEvent;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static by.klevitov.eventpersistor.util.EntityConverterUtil.extractLocationFromEventDTO;
import static by.klevitov.eventpersistor.util.EntityConverterUtil.throwExceptionInCaseOfNullDTO;
import static by.klevitov.eventpersistor.util.EntityConverterUtil.throwExceptionInCaseOfNullEntity;

@Component
public class AfishaRelaxEventConverter implements EntityConverter {
    private final LocationConverter locationConverter;

    @Autowired
    public AfishaRelaxEventConverter(LocationConverter locationConverter) {
        this.locationConverter = locationConverter;
    }

    @Override
    public AbstractEntity convertFromDTO(final AbstractDTO dto) {
        throwExceptionInCaseOfNullDTO(dto);
        AfishaRelaxEventDTO eventDTO = (AfishaRelaxEventDTO) dto;
        return AfishaRelaxEvent.builder()
                .id(eventDTO.getId())
                .title(eventDTO.getTitle())
                .location(extractLocationFromEventDTO(eventDTO, locationConverter))
                .dateStr(eventDTO.getDateStr())
                .date(eventDTO.getDate())
                .category(eventDTO.getCategory())
                .sourceType(eventDTO.getSourceType())
                .eventLink(eventDTO.getEventLink())
                .imageLink(eventDTO.getImageLink())
                .build();
    }

    @Override
    public AbstractDTO convertToDTO(final AbstractEntity entity) {
        throwExceptionInCaseOfNullEntity(entity);
        AfishaRelaxEvent event = (AfishaRelaxEvent) entity;
        return AfishaRelaxEventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .location((LocationDTO) locationConverter.convertToDTO(event.getLocation()))
                .dateStr(event.getDateStr())
                .date(event.getDate())
                .category(event.getCategory())
                .sourceType(event.getSourceType())
                .eventLink(event.getEventLink())
                .imageLink(event.getImageLink())
                .build();
    }
}
