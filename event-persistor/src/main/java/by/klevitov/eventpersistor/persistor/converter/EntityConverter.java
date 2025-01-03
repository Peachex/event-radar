package by.klevitov.eventpersistor.persistor.converter;

import by.klevitov.eventpersistor.persistor.entity.AbstractEntity;
import by.klevitov.eventradarcommon.dto.AbstractDTO;

public interface EntityConverter {
    AbstractEntity convertFromDTO(AbstractDTO dto);

    AbstractDTO convertToDTO(AbstractEntity entity);
}
