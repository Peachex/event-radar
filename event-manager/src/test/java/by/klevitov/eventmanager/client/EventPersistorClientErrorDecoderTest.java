package by.klevitov.eventmanager.client;

import by.klevitov.eventradarcommon.client.EventPersistorClientErrorDecoder;
import by.klevitov.eventradarcommon.client.exception.EventPersistorClientException;
import by.klevitov.eventradarcommon.client.util.EventPersistorClientUtil;
import by.klevitov.eventradarcommon.exception.ExceptionResponse;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static by.klevitov.eventmanager.constant.ManagerExceptionMessage.EVENT_PERSISTOR_CLIENT_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EventPersistorClientErrorDecoderTest {
    private EventPersistorClientErrorDecoder errorDecoder;
    private EventPersistorClientUtil mockedClientUtil;

    @BeforeEach
    public void setUp() {
        mockedClientUtil = Mockito.mock(EventPersistorClientUtil.class);
        errorDecoder = new EventPersistorClientErrorDecoder(mockedClientUtil);
    }

    @Test
    public void test_decode() throws Exception {
        Response mockedResponse = Mockito.mock(Response.class);
        Response.Body mockedBody = Mockito.mock(Response.Body.class);
        LocalDateTime currentDateTime = LocalDateTime.now();
        ExceptionResponse exceptionResponse = new ExceptionResponse(currentDateTime, 404, "exceptionMessage",
                "exceptionClass", "rootStackTrace");

        when(mockedResponse.body())
                .thenReturn(mockedBody);
        when(mockedBody.asInputStream())
                .thenReturn(null);
        when(mockedClientUtil.extractResponseFromBody(any(), any()))
                .thenReturn(exceptionResponse);

        Exception expected = new EventPersistorClientException(exceptionResponse,
                String.format(EVENT_PERSISTOR_CLIENT_EXCEPTION,
                        exceptionResponse.getStatus(),
                        exceptionResponse.getTimestamp(),
                        exceptionResponse.getExceptionMessage(),
                        exceptionResponse.getExceptionClass(),
                        exceptionResponse.getRootStackTrace()));
        Exception actual = errorDecoder.decode("methodKey", mockedResponse);
        verifyExceptions(expected, actual);
    }

    private void verifyExceptions(final Exception expected, final Exception actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getClass(), actual.getClass());
    }
}
