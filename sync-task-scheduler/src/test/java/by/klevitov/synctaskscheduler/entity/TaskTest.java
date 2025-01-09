package by.klevitov.synctaskscheduler.entity;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static by.klevitov.synctaskscheduler.entity.TaskStatus.ACTIVE;
import static by.klevitov.synctaskscheduler.entity.TaskStatus.PAUSED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void test_copyValuesForNullOrEmptyFieldsFromTask(Pair<Task, Pair<Task, Task>> doublePair) {
        Task existentTask = doublePair.getKey();
        Task actual = doublePair.getValue().getValue();
        actual.copyValuesForNullOrEmptyFieldsFromTask(existentTask);
        Task expected = doublePair.getValue().getKey();
        assertEquals(expected, actual);
    }

    private static Stream<Pair<Task, Pair<Task, Task>>> doublePairProvider() {
        return Stream.of(
                Pair.of(new Task(1, ACTIVE, "taskName", null, "taskIdToExecute", "cronExpression"),
                        Pair.of(new Task(1, ACTIVE, "updatedTaskName", null, "taskIdToExecute", "updatedCronExpression"),
                                new Task(1, null, "updatedTaskName", null, null, "updatedCronExpression"))),
                Pair.of(new Task(1, ACTIVE, "taskName", null, "taskIdToExecute", "cronExpression"),
                        Pair.of(new Task(1, PAUSED, "taskName", null, "taskIdToExecute", "cronExpression"),
                                new Task(1, PAUSED, null, null, null, null)))
        );
    }

    @Test
    public void test_createTaskIdentityName() {
        Task task = new Task(1, ACTIVE, "taskName", null, "taskIdToExecute", "cronExpression");
        String expected = "1_task";
        String actual = task.createTaskIdentityName();
        assertEquals(expected, actual);
    }

    @Test
    public void test_createTriggerIdentityName() {
        Task task = new Task(1, ACTIVE, "taskName", null, "taskIdToExecute", "cronExpression");
        String expected = "1_trigger";
        String actual = task.createTriggerIdentityName();
        assertEquals(expected, actual);
    }
}
