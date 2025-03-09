package by.klevitov.eventpersistor.common.dto;

import by.klevitov.eventpersistor.entity.AbstractEntity;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Data
public class PageResponseDTO<D extends AbstractDTO> {
    private Page<D> page;

    public PageResponseDTO(Page<? extends AbstractEntity> entityResultPage, List<D> entitiesDTO) {
        this.page = new PageImpl<>(entitiesDTO, entityResultPage.getPageable(), entityResultPage.getTotalElements());
    }
}
