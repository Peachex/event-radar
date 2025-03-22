package by.klevitov.eventradarcommon.pagination.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskManagerResponseDTOTest {
    @Test
    public void test_createListFromTaskIds_withNullList() {
        List<TaskManagerResponseDTO> expected = new ArrayList<>();
        List<TaskManagerResponseDTO> actual = TaskManagerResponseDTO.createListFromTaskIds(null);
        assertEquals(expected, actual);
    }

    @Test
    public void test_createListFromTaskIds_withEmptyList() {
        List<TaskManagerResponseDTO> expected = new ArrayList<>();
        List<TaskManagerResponseDTO> actual = TaskManagerResponseDTO.createListFromTaskIds(new ArrayList<>());
        assertEquals(expected, actual);
    }

    @Test
    public void test_createListFromTaskIds_withValidList() {
        List<TaskManagerResponseDTO> expected = List.of(
                new TaskManagerResponseDTO("id1"),
                new TaskManagerResponseDTO("id2")
        );
        List<TaskManagerResponseDTO> actual = TaskManagerResponseDTO.createListFromTaskIds(List.of("id1", "id2"));
        assertEquals(expected, actual);
    }
}
