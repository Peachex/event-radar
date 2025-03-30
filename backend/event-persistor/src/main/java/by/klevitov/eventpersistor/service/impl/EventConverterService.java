package by.klevitov.eventpersistor.service.impl;

import by.klevitov.eventpersistor.converter.EntityConverter;
import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.service.EntityConverterService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventConverterService implements EntityConverterService<AbstractEvent, AbstractEventDTO> {
    private final EntityConverterFactory converterFactory;

    public EventConverterService(EntityConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public AbstractEvent convertFromDTO(AbstractEventDTO dto) {
        EntityConverter converter = converterFactory.getConverter(dto.getSourceType());
        return (AbstractEvent) converter.convertFromDTO(dto);
    }

    @Override
    public List<AbstractEvent> convertFromDTO(List<AbstractEventDTO> dto) {
        return dto.stream()
                .map(d -> (AbstractEvent) converterFactory.getConverter(d.getSourceType()).convertFromDTO(d))
                .toList();
    }

    @Override
    public AbstractEventDTO convertToDTO(AbstractEvent entity) {
        EntityConverter converter = converterFactory.getConverter(entity.getSourceType());
        return (AbstractEventDTO) converter.convertToDTO(entity);
    }

    @Override
    public List<AbstractEventDTO> convertToDTO(List<AbstractEvent> entities) {
        return entities.stream()
                .map(e -> (AbstractEventDTO) converterFactory.getConverter(e.getSourceType()).convertToDTO(e))
                .toList();
    }
}
