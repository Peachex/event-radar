package by.klevitov.eventpersistor.messaging.request.handler;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;

public interface RequestHandler {
    MessageResponse handle(EntityData entityData);
}
