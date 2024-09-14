package by.klevitov.eventradarcommon.util;

import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public final class MessageConverter {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public static byte[] convertRequestToBytes(final MessageRequest request) throws JsonProcessingException {
        return mapper.writeValueAsBytes(request);
    }

    public static MessageRequest convertRequestFromBytes(final byte[] request) throws IOException {
        return mapper.readValue(request, MessageRequest.class);
    }

    public static byte[] convertResponseToBytes(final MessageResponse response) throws JsonProcessingException {
        return mapper.writeValueAsBytes(response);
    }

    public static MessageResponse convertResponseFromBytes(final byte[] response) throws IOException {
        return mapper.readValue(response, MessageResponse.class);
    }
}
