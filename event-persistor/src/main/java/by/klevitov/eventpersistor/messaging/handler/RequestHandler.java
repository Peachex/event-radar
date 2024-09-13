package by.klevitov.eventpersistor.messaging.handler;

import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;

public interface RequestHandler {
    MessageResponse handle(EntityData entityData);

    //todo create factory for converting event/location from DTO to entity classes and
    // use is it in handlers.
}
