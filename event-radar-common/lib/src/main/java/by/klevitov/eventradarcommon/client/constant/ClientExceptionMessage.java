package by.klevitov.eventradarcommon.client.constant;

public final class ClientExceptionMessage {
    public static final String RESPONSE_DESERIALIZATION_FAILED = "Failed to deserialize response body of type: '%s'.";
    public static final String EVENT_PERSISTOR_CLIENT_EXCEPTION = "Status code: '%s', time: '%s', exception message: "
            + "'%s', exception class: '%s', root stack trace: '%s'.";

    private ClientExceptionMessage() {
    }
}
