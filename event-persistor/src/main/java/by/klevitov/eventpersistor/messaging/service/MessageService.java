package by.klevitov.eventpersistor.messaging.service;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.MessageRequest;

public interface MessageService {
    Object processAndRetrieveResult(final MessageRequest messageRequest);
}
