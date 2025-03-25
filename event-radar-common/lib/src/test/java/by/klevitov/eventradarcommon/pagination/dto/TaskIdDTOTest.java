package by.klevitov.eventradarcommon.pagination.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskIdDTOTest {
    @Test
    public void test_createListFromTaskIds_withNullList() {
        List<TaskIdDTO> expected = new ArrayList<>();
        List<TaskIdDTO> actual = TaskIdDTO.createListFromTaskIds(null);
        assertEquals(expected, actual);
    }

    @Test
    public void test_createListFromTaskIds_withEmptyList() {
        List<TaskIdDTO> expected = new ArrayList<>();
        List<TaskIdDTO> actual = TaskIdDTO.createListFromTaskIds(new ArrayList<>());
        assertEquals(expected, actual);
    }

    @Test
    public void test_createListFromTaskIds_withValidList() {
        List<TaskIdDTO> expected = List.of(
                new TaskIdDTO("id1"),
                new TaskIdDTO("id2")
        );
        List<TaskIdDTO> actual = TaskIdDTO.createListFromTaskIds(List.of("id1", "id2"));
        assertEquals(expected, actual);
    }
}
