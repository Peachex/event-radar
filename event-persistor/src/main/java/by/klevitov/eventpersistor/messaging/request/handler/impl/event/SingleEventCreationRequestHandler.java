package by.klevitov.eventpersistor.messaging.request.handler.impl.event;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.data.SingleEventData;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.SuccessfulMessageResponse;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.INVALID_ENTITY_EVENT_DATA;

@Log4j2
@Component
public class SingleEventCreationRequestHandler implements RequestHandler {
    private final EventService service;

    @Autowired
    public SingleEventCreationRequestHandler(EventService service) {
        this.service = service;
    }

    @Override
    public MessageResponse handle(EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        SingleEventData eventData = (SingleEventData) entityData;
        AbstractEvent eventToCreate = eventData.getEventDTO();
        AbstractEvent createdEvent = service.create(eventToCreate);
        return new SuccessfulMessageResponse(new SingleEventData(createdEvent));
    }

    private void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData)) {
            final String exceptionMessage = String.format(INVALID_ENTITY_EVENT_DATA, entityData);
            log.error(exceptionMessage);
            throw new RequestHandlerException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof SingleEventData);
    }
}
