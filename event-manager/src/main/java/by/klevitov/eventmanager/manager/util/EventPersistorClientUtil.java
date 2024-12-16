package by.klevitov.eventmanager.manager.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class EventPersistorClientUtil {
    private final ObjectMapper objectMapper;

    @Autowired
    public EventPersistorClientUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T extractResponseFromBody(InputStream body, final Class<T> clazz) throws IOException {
        return objectMapper.readValue(body, clazz);
    }
}
