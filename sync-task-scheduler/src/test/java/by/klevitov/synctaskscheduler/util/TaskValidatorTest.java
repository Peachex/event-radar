package by.klevitov.synctaskscheduler.util;

import by.klevitov.synctaskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.entity.TaskStatus;
import by.klevitov.synctaskscheduler.exception.TaskValidatorException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static by.klevitov.synctaskscheduler.entity.TaskStatus.ACTIVE;
import static by.klevitov.synctaskscheduler.util.TaskValidator.throwExceptionInCaseOfNullStatus;
import static by.klevitov.synctaskscheduler.util.TaskValidator.validateCronExpression;
import static by.klevitov.synctaskscheduler.util.TaskValidator.validateTaskBeforeCreation;
import static by.klevitov.synctaskscheduler.util.TaskValidator.validateTaskBeforeUpdating;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TaskValidatorTest {
    @ParameterizedTest
    @MethodSource("tasksForCreation")
    public void test_validateTaskBeforeCreation(Pair<Task, Boolean> tasksWithExpectedValues) {
        Task task = tasksWithExpectedValues.getKey();
        boolean taskIsValid = tasksWithExpectedValues.getValue();
        if (taskIsValid) {
            assertDoesNotThrow(() -> validateTaskBeforeCreation(task));
        } else {
            assertThrowsExactly(TaskValidatorException.class, () -> validateTaskBeforeCreation(task));
        }
    }

    private static Stream<Pair<Task, Boolean>> tasksForCreation() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of(new Task(), false),
                Pair.of(new Task(1, ACTIVE, "taskName", null, null, null), false),
                Pair.of(new Task(1, ACTIVE, "taskName", null, "taskIdToExecute", null), false),
                Pair.of(new Task(1, ACTIVE, "taskName", null, "taskIdToExecute", "invalidCronExpression"), false),
                Pair.of(new Task(1, ACTIVE, "taskName", null, "taskIdToExecute", "0/3 * * * * ?"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("tasksForUpdating")
    public void test_validateTaskBeforeUpdating(Pair<Task, Boolean> tasksWithExpectedValues) {
        Task task = tasksWithExpectedValues.getKey();
        boolean taskIsValid = tasksWithExpectedValues.getValue();
        if (taskIsValid) {
            assertDoesNotThrow(() -> validateTaskBeforeUpdating(task));
        } else {
            assertThrowsExactly(TaskValidatorException.class, () -> validateTaskBeforeUpdating(task));
        }
    }

    private static Stream<Pair<Task, Boolean>> tasksForUpdating() {
        return Stream.of(
                Pair.of(null, false),
                Pair.of(new Task(), false),
                Pair.of(new Task(1, ACTIVE, "taskName", null, "taskIdToExecute", "invalidCronExpression"), false),
                Pair.of(new Task(1, ACTIVE, "taskName", null, null, null), true),
                Pair.of(new Task(1, ACTIVE, null, null, "taskIdToExecute", null), true),
                Pair.of(new Task(1, ACTIVE, null, null, null, "0/3 * * * * ?"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("cronExpressions")
    public void test_validateCronExpression(Pair<String, Boolean> cronExpressionsWithExpectedValues) {
        String cronExpression = cronExpressionsWithExpectedValues.getKey();
        boolean cronExpressionIsValid = cronExpressionsWithExpectedValues.getValue();
        if (cronExpressionIsValid) {
            assertDoesNotThrow(() -> validateCronExpression(cronExpression));
        } else {
            assertThrowsExactly(TaskValidatorException.class, () -> validateCronExpression(cronExpression));
        }
    }

    private static Stream<Pair<String, Boolean>> cronExpressions() {
        return Stream.of(
                Pair.of("invalidCronExpression", false),
                Pair.of("0/3 * * * * ?", true)
        );
    }

    @Test
    public void test_throwExceptionInCaseOfNullStatus_WithNotNullStatus() {
        TaskStatus status = ACTIVE;
        assertDoesNotThrow(() -> throwExceptionInCaseOfNullStatus(status));
    }

    @Test
    public void test_throwExceptionInCaseOfNullStatus_WithNullStatus() {
        TaskStatus status = null;
        assertThrowsExactly(TaskValidatorException.class, () -> throwExceptionInCaseOfNullStatus(status));
    }
}
