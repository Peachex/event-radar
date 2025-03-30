package by.klevitov.eventradarcommon.pagination.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    private int page;
    private int size;
    private List<SortField> sortFields;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortField {
        private String field;
        private String direction;
    }

    public PageRequest createPageRequest() {
        return isEmpty(sortFields)
                ? PageRequest.of(this.page, this.size)
                : PageRequest.of(this.page, this.size, Sort.by(createOrders()));
    }

    private List<Sort.Order> createOrders() {
        return sortFields.stream()
                .map(s -> new Sort.Order(Sort.Direction.fromString(s.getDirection()), s.getField()))
                .collect(Collectors.toList());
    }
}
