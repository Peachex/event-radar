package by.klevitov.eventwebapp.util;

import by.klevitov.eventradarcommon.client.util.EventPersistorClientUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EventPersistorClientUtilTest {
    private EventPersistorClientUtil clientUtil;
    private ObjectMapper mockedObjectMapper;

    @BeforeEach
    public void setUp() {
        mockedObjectMapper = Mockito.mock(ObjectMapper.class);
        clientUtil = new EventPersistorClientUtil(mockedObjectMapper);
    }

    @Test
    public void test_extractResponseFromBody() throws IOException {
        InputStream body = InputStream.nullInputStream();
        clientUtil.extractResponseFromBody(body, Object.class);
        verify(mockedObjectMapper, times(1)).readValue(body, Object.class);
    }
}
