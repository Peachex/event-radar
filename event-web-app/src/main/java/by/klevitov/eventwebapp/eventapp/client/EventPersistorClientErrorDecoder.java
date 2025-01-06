package by.klevitov.eventwebapp.eventapp.client;

import by.klevitov.eventradarcommon.exception.ExceptionResponse;
import by.klevitov.eventwebapp.eventapp.exception.EventPersistorClientException;
import by.klevitov.eventwebapp.eventapp.exception.ResponseDeserializationException;
import by.klevitov.eventwebapp.eventapp.util.EventPersistorClientUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import static by.klevitov.eventwebapp.eventapp.constant.ManagerExceptionMessage.EVENT_PERSISTOR_CLIENT_EXCEPTION;
import static by.klevitov.eventwebapp.eventapp.constant.ManagerExceptionMessage.RESPONSE_DESERIALIZATION_FAILED;

@Log4j2
@Component
public class EventPersistorClientErrorDecoder implements ErrorDecoder {
    private static final Class<ExceptionResponse> EXCEPTION_RESPONSE_CLASS = ExceptionResponse.class;

    private final EventPersistorClientUtil clientUtil;

    @Autowired
    public EventPersistorClientErrorDecoder(EventPersistorClientUtil clientUtil) {
        this.clientUtil = clientUtil;
    }

    @Override
    public Exception decode(final String methodKey, final Response response) {
        try {
            final InputStream body = response.body().asInputStream();
            ExceptionResponse exceptionResponse = clientUtil.extractResponseFromBody(body, EXCEPTION_RESPONSE_CLASS);
            log.error(exceptionResponse);
            return createClientExceptionBasedOnExceptionResponse(exceptionResponse);
        } catch (IOException e) {
            final String exceptionMessage = String.format(RESPONSE_DESERIALIZATION_FAILED, EXCEPTION_RESPONSE_CLASS);
            log.error(exceptionMessage);
            throw new ResponseDeserializationException(exceptionMessage, e);
        }
    }

    private EventPersistorClientException createClientExceptionBasedOnExceptionResponse(ExceptionResponse response) {
        return new EventPersistorClientException(response,
                String.format(EVENT_PERSISTOR_CLIENT_EXCEPTION,
                        response.getStatus(),
                        response.getTimestamp(),
                        response.getExceptionMessage(),
                        response.getExceptionClass(),
                        response.getRootStackTrace()));
    }
}
