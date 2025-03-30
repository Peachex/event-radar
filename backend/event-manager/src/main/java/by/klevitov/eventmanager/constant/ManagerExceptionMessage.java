package by.klevitov.eventmanager.constant;

public final class ManagerExceptionMessage {
    public static final String TASK_EXECUTOR_NOT_FOUND = "Task executor not found for: '%s'";
    public static final String NULL_OR_EMPTY_TASK_TO_EXECUTE = "Task id to execute cannot be null or empty.";
    public static final String RESPONSE_DESERIALIZATION_FAILED = "Failed to deserialize response body of type: '%s'.";
    public static final String EVENT_PERSISTOR_CLIENT_EXCEPTION = "Status code: '%s', time: '%s', exception message: "
            + "'%s', exception class: '%s', root stack trace: '%s'.";
    public static final String TASK_EXECUTION_ERROR = "An error occurs while executing: '%s'. Exception message: '%s'.";

    private ManagerExceptionMessage() {
    }
}
