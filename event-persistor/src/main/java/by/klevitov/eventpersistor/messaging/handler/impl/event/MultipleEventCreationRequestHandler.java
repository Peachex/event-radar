package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleEventData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.INVALID_ENTITY_EVENTS_DATA;

@Log4j2
@Component
public class MultipleEventCreationRequestHandler implements RequestHandler {
    private final EventService service;
    private final EntityConverterFactory converterFactory;

    @Autowired
    public MultipleEventCreationRequestHandler(EventService service, EntityConverterFactory converterFactory) {
        this.service = service;
        this.converterFactory = converterFactory;
    }

    @Override
    public MessageResponse handle(final EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        List<AbstractEventDTO> eventsDTO = ((MultipleEventData) entityData).getEventsDTO();
        List<AbstractEvent> eventsToCreate = retrieveEventsToCreate(eventsDTO);
        List<AbstractEvent> createdEvents = service.create(eventsToCreate);
        List<AbstractEventDTO> createdEventsDTO = retrieveCreatedEventsDTO(createdEvents);
        return new SuccessfulMessageResponse(new MultipleEventData(createdEventsDTO));
    }

    public void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData) || entityDataContainsNullData(entityData)) {
            final String exceptionMessage = String.format(INVALID_ENTITY_EVENTS_DATA, entityData);
            log.error(exceptionMessage);
            throw new RequestHandlerException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof MultipleEventData);
    }

    private boolean entityDataContainsNullData(final EntityData entityData) {
        return ((MultipleEventData) entityData).getEventsDTO() == null;
    }

    private List<AbstractEvent> retrieveEventsToCreate(final List<AbstractEventDTO> eventsDTO) {
        return eventsDTO.stream().map(e -> {
            EntityConverter converter = converterFactory.getConverter(e.getClass());
            return (AbstractEvent) converter.convertFromDTO(e);
        }).toList();
    }

    private List<AbstractEventDTO> retrieveCreatedEventsDTO(final List<AbstractEvent> createdEvents) {
        return createdEvents.stream().map(e -> {
            EntityConverter converter = converterFactory.getConverter(e.getSourceType());
            return (AbstractEventDTO) converter.convertToDTO(e);
        }).toList();
    }
}
