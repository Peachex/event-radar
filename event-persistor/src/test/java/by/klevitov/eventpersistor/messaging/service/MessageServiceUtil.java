package by.klevitov.eventpersistor.messaging.service;

import by.klevitov.eventradarcommon.messaging.response.ErrorMessageResponse;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MessageServiceUtil {
    private MessageServiceUtil() {
    }

    public static void assertEqualsExcludingCreatedDateAndRequestId(final MessageResponse expected,
                                                                    final MessageResponse actual) {
        resetCreatedDate(expected);
        resetRequestId(expected);
        resetCreatedDate(actual);
        resetRequestId(actual);
        assertEquals(expected, actual);
    }

    public static void assertEqualsExcludingCreatedDateAndRequestIdAndThrowable(final MessageResponse expected,
                                                                                final MessageResponse actual) {

        resetCreatedDate(expected);
        resetRequestId(expected);
        ((ErrorMessageResponse) expected).setThrowable(null);
        resetCreatedDate(actual);
        resetRequestId(actual);
        ((ErrorMessageResponse) actual).setThrowable(null);
        assertEquals(expected, actual);
    }

    private static void resetCreatedDate(final MessageResponse response) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        response.setRequestCreatedDate(currentDateTime);
        response.setResponseCreatedDate(currentDateTime);
    }

    private static void resetRequestId(final MessageResponse response) {
        response.setRequestId("requestId");
    }
}
