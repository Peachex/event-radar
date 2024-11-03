package by.klevitov.eventpersistor.persistor.converter.impl;

import by.klevitov.eventpersistor.persistor.converter.EntityConverter;
import by.klevitov.eventpersistor.persistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.persistor.entity.ByCardEvent;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static by.klevitov.eventpersistor.persistor.util.EntityConverterUtil.extractLocationFromEventDTO;
import static by.klevitov.eventpersistor.persistor.util.EntityConverterUtil.throwExceptionInCaseOfNullDTO;
import static by.klevitov.eventpersistor.persistor.util.EntityConverterUtil.throwExceptionInCaseOfNullEntity;

@Component
public class ByCardEventConverter implements EntityConverter {
    private final LocationConverter locationConverter;

    @Autowired
    public ByCardEventConverter(LocationConverter locationConverter) {
        this.locationConverter = locationConverter;
    }

    @Override
    public AbstractEntity convertFromDTO(final AbstractDTO dto) {
        throwExceptionInCaseOfNullDTO(dto);
        ByCardEventDTO eventDTO = (ByCardEventDTO) dto;
        return ByCardEvent.builder()
                .id(eventDTO.getId())
                .title(eventDTO.getTitle())
                .location(extractLocationFromEventDTO(eventDTO, locationConverter))
                .dateStr(eventDTO.getDateStr())
                .date(eventDTO.getDate())
                .category(eventDTO.getCategory())
                .sourceType(eventDTO.getSourceType())
                .priceStr(eventDTO.getPriceStr())
                .price(eventDTO.getPrice())
                .eventLink(eventDTO.getEventLink())
                .imageLink(eventDTO.getImageLink())
                .build();
    }

    @Override
    public AbstractDTO convertToDTO(final AbstractEntity entity) {
        throwExceptionInCaseOfNullEntity(entity);
        ByCardEvent event = (ByCardEvent) entity;
        return ByCardEventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .location((LocationDTO) locationConverter.convertToDTO(event.getLocation()))
                .dateStr(event.getDateStr())
                .date(event.getDate())
                .category(event.getCategory())
                .sourceType(event.getSourceType())
                .priceStr(event.getPriceStr())
                .price(event.getPrice())
                .eventLink(event.getEventLink())
                .imageLink(event.getImageLink())
                .build();
    }
}
