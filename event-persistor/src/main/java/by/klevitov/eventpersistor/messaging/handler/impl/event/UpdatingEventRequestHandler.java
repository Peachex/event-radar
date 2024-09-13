package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.SingleEventData;
import by.klevitov.eventradarcommon.messaging.request.data.SingleEventWithIdData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.INVALID_ENTITY_EVENTS_DATA;

@Log4j2
@Component
public class UpdatingEventRequestHandler implements RequestHandler {
    private final EventService service;
    private final EntityConverterFactory converterFactory;

    @Autowired
    public UpdatingEventRequestHandler(EventService service, EntityConverterFactory converterFactory) {
        this.service = service;
        this.converterFactory = converterFactory;
    }

    @Override
    public MessageResponse handle(final EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        AbstractEventDTO eventDTO = ((SingleEventWithIdData) entityData).getEventDTO();
        EntityConverter converter = converterFactory.getConverter(eventDTO.getSourceType());
        AbstractEvent eventToUpdate = (AbstractEvent) converter.convertFromDTO(eventDTO);
        eventToUpdate.setId(((SingleEventWithIdData) entityData).getId());
        AbstractEvent updatedEvent = service.update(eventToUpdate);
        AbstractEventDTO updatedEventDTO = (AbstractEventDTO) converter.convertToDTO(updatedEvent);
        return new SuccessfulMessageResponse(new SingleEventData(updatedEventDTO));
    }

    public void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData) || entityDataContainsNullData(entityData)) {
            final String exceptionMessage = String.format(INVALID_ENTITY_EVENTS_DATA, entityData);
            log.error(exceptionMessage);
            throw new RequestHandlerException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof SingleEventWithIdData);
    }

    private boolean entityDataContainsNullData(final EntityData entityData) {
        return ((SingleEventWithIdData) entityData).getEventDTO() == null;
    }
}
