package by.klevitov.eventpersistor.converter;

import by.klevitov.eventpersistor.entity.AbstractEntity;
import by.klevitov.eventradarcommon.dto.AbstractDTO;

public interface EntityConverter {
    AbstractEntity convertFromDTO(AbstractDTO dto);

    AbstractDTO convertToDTO(AbstractEntity entity);
}
