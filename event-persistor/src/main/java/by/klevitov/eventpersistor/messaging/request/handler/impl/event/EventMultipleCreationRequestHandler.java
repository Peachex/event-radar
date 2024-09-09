package by.klevitov.eventpersistor.messaging.request.handler.impl.event;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.data.MultipleEventData;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class EventMultipleCreationRequestHandler implements RequestHandler {
    private final EventService service;

    @Autowired
    public EventMultipleCreationRequestHandler(EventService service) {
        this.service = service;
    }

    @Override
    public MessageResponse handle(final EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        MultipleEventData eventData = (MultipleEventData) entityData;
        List<AbstractEvent> eventsToCreate = eventData.getEvents();
        List<AbstractEvent> createdEvents = service.create(eventsToCreate);
        return new MessageResponse("responseId", new MultipleEventData(createdEvents));
    }

    public void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData)) {
            final String exceptionMessage = "Invalid data for creating events: " + entityData;
            log.error(exceptionMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof MultipleEventData);
    }

    //todo Add appropriate exception. Also add response id generator. Move common methods for all handlers to
    // separate class.
}
