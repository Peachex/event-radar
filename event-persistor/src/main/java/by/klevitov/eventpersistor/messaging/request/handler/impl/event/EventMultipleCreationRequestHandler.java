package by.klevitov.eventpersistor.messaging.request.handler.impl.event;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.data.MultipleEventData;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.ErrorMessageResponse;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.exception.AbstractServiceException;
import by.klevitov.eventpersistor.persistor.exception.EventValidatorException;
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
        if (entityDataIsNotValid(entityData)) {
            Exception e = new IllegalArgumentException("Invalid data for creating events.");
            return createErrorResponseAndLogException(e, "Exception during request handling: {}");
        }
        MultipleEventData eventData = (MultipleEventData) entityData;
        return processEventsCreationAndCreateResponse(eventData);
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof MultipleEventData);
    }

    private ErrorMessageResponse createErrorResponseAndLogException(final Exception e, final String exceptionMessage) {
        log.error(exceptionMessage, e.getMessage());
        return new ErrorMessageResponse("responseId", e.getMessage(), e);
    }

    private MessageResponse processEventsCreationAndCreateResponse(final MultipleEventData eventData) {
        MessageResponse response;
        try {
            List<AbstractEvent> eventsToCreate = eventData.getEvents();
            List<AbstractEvent> createdEvents = service.create(eventsToCreate);
            response = new MessageResponse("responseId", new MultipleEventData(createdEvents));
        } catch (AbstractServiceException | EventValidatorException e) {
            response = createErrorResponseAndLogException(e, "Exception during request handling: {}");
        }
        return response;
    }

    //todo Add appropriate exception. Also add response id generator. Move common methods for all handlers to
    // separate class.
}
