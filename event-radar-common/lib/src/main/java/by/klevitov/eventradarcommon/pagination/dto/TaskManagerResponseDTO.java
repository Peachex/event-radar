package by.klevitov.eventradarcommon.pagination.dto;

import by.klevitov.eventradarcommon.dto.AbstractDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskManagerResponseDTO extends AbstractDTO {
    private String taskId;

    public static List<TaskManagerResponseDTO> createListFromTaskIds(final List<String> ids) {
        return (isNotEmpty(ids) ? createList(ids) : new ArrayList<>());
    }

    private static List<TaskManagerResponseDTO> createList(final List<String> ids) {
        return ids.stream()
                .map(TaskManagerResponseDTO::new)
                .toList();
    }
}
