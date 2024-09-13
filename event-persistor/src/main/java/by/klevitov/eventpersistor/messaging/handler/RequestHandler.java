package by.klevitov.eventpersistor.messaging.handler;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;

public interface RequestHandler {
    MessageResponse handle(EntityData entityData);

    //todo create factory for converting event/location from DTO to entity classes and
    // use is it in handlers.
}
