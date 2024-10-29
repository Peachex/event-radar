package by.klevitov.eventpersistor.persistor.service;

import by.klevitov.eventpersistor.persistor.entity.AbstractEntity;
import by.klevitov.eventradarcommon.dto.AbstractDTO;

import java.util.List;

public interface ConverterService<E extends AbstractEntity, D extends AbstractDTO> {
    E convertFromDTO(D dto);

    List<E> convertFromDTO(List<D> dto);

    D convertToDTO(E entity);

    List<D> convertToDTO(List<E> entities);

    //todo Add unit tests.
}
