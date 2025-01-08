package by.klevitov.eventwebapp.constant;

public final class ManagerExceptionMessage {
    public static final String RESPONSE_DESERIALIZATION_FAILED = "Failed to deserialize response body of type: '%s'.";
    public static final String EVENT_PERSISTOR_CLIENT_EXCEPTION = "Status code: '%s', time: '%s', exception message: "
            + "'%s', exception class: '%s', root stack trace: '%s'.";

    private ManagerExceptionMessage() {
    }
}
