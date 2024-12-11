package by.klevitov.eventmanager.manager.client;

import by.klevitov.eventradarcommon.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class EventPersistorClientErrorDecoder implements ErrorDecoder {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionResponse exceptionResponse;
        try {
            exceptionResponse = objectMapper.readValue(response.body().asInputStream(), ExceptionResponse.class);
            log.error(exceptionResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Exception(String.format("Status code: %s, time: %s, exception message: %s, exception class: %s,"
                        + " root stack trace: %s",
                exceptionResponse.getStatus(),
                exceptionResponse.getTimestamp(),
                exceptionResponse.getExceptionMessage(),
                exceptionResponse.getExceptionClass(),
                exceptionResponse.getRootStackTrace()));
    }

    //todo Add appropriate exception handling. May be throw custom exception or just log response or parse response to
    // common exception object (event-radar-common). Move mapper logic to separate class (may be common module).
}
