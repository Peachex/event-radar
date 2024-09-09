package by.klevitov.eventpersistor.messaging.service;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.MessageRequest;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;

public interface MessageService {
    MessageResponse processAndRetrieveResult(final MessageRequest messageRequest);
}
