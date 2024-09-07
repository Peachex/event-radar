package by.klevitov.eventpersistor.messaging.request.handler.impl.event;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.data.MultipleEventData;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventMultipleCreationRequestHandler implements RequestHandler {
    private final EventService service;

    @Autowired
    public EventMultipleCreationRequestHandler(EventService service) {
        this.service = service;
    }

    @Override
    public Object handle(EntityData entityData) {
        if (entityData instanceof MultipleEventData data) {
            List<AbstractEvent> events = data.getEvents();
            return service.create(events);
        }
        throw new IllegalArgumentException("Invalid data for creating events.");
    }

    //todo Add MessageResponse and add appropriate exception.
}
