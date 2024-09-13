package by.klevitov.eventpersistor.messaging.service;

import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;

public interface MessageService {
    MessageResponse processAndRetrieveResult(final MessageRequest messageRequest);
}
