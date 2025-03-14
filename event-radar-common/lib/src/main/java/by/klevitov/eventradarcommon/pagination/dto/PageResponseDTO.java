package by.klevitov.eventradarcommon.pagination.dto;

import by.klevitov.eventradarcommon.dto.AbstractDTO;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Data
public class PageResponseDTO<D extends AbstractDTO> {
    private Page<D> page;

    public PageResponseDTO(Page<?> entityResultPage, List<D> entitiesDTO) {
        this.page = new PageImpl<>(entitiesDTO, entityResultPage.getPageable(), entityResultPage.getTotalElements());
    }
}
