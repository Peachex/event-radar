package by.klevitov.eventpersistor.service;

import by.klevitov.eventpersistor.entity.AbstractEntity;
import by.klevitov.eventradarcommon.dto.AbstractDTO;

import java.util.List;

public interface EntityConverterService<E extends AbstractEntity, D extends AbstractDTO> {
    E convertFromDTO(D dto);

    List<E> convertFromDTO(List<D> dto);

    D convertToDTO(E entity);

    List<D> convertToDTO(List<E> entities);
}
