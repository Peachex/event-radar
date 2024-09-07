package by.klevitov.eventpersistor.messaging.request.handler;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;

public interface RequestHandler {
    Object handle(EntityData entityData);
}
