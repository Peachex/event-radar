package by.klevitov.eventpersistor.messaging.handler;

import by.klevitov.eventradarcommon.messaging.response.MessageResponse;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class RequestHandlerUtil {
    private RequestHandlerUtil() {
    }

    public static void assertEqualsExcludingCreatedDate(final MessageResponse expected, final MessageResponse actual) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        expected.setResponseCreatedDate(currentDateTime);
        actual.setResponseCreatedDate(currentDateTime);
        assertEquals(expected, actual);
    }
}
