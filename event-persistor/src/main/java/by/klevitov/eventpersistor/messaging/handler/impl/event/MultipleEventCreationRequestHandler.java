package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.data.MultipleEventData;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.SuccessfulMessageResponse;
import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
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
        List<AbstractEventDTO> createdEventsDTO = retrieveCreatedEventsDTO(createdEvents, eventsDTO);
        return new SuccessfulMessageResponse(new MultipleEventData(createdEventsDTO));
    }

    public void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData)) {
            final String exceptionMessage = String.format(INVALID_ENTITY_EVENTS_DATA, entityData);
            log.error(exceptionMessage);
            throw new RequestHandlerException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof MultipleEventData);
    }

    private List<AbstractEvent> retrieveEventsToCreate(final List<AbstractEventDTO> eventsDTO) {
        return eventsDTO.stream().map(e -> {
            EntityConverter converter = converterFactory.getConverter(e.getClass());
            return (AbstractEvent) converter.convertFromDTO(e);
        }).toList();
    }

    private List<AbstractEventDTO> retrieveCreatedEventsDTO(final List<AbstractEvent> createdEvents,
                                                            final List<AbstractEventDTO> eventsDTO) {
        return createdEvents.stream().map(e -> {
            Class<? extends AbstractDTO> eventDTOClass = findEventDTOClassBySourceType(e.getSourceType(), eventsDTO);
            EntityConverter converter = converterFactory.getConverter(eventDTOClass);
            return (AbstractEventDTO) converter.convertToDTO(e);
        }).toList();
    }

    private Class<? extends AbstractDTO> findEventDTOClassBySourceType(final EventSourceType sourceType,
                                                                       final List<AbstractEventDTO> eventsDTO) {
        return eventsDTO.stream()
                .filter(e -> e.getSourceType().equals(sourceType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find event dto class value!"))
                .getClass();
    }
}
