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
import by.klevitov.eventradarcommon.messaging.request.data.SearchFieldsData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.INVALID_SEARCH_BY_FIELDS_DATA;

@Log4j2
@Component
public class SearchByFieldsEventRequestHandler implements RequestHandler {
    private final EventService service;
    private final EntityConverterFactory converterFactory;

    @Autowired
    public SearchByFieldsEventRequestHandler(EventService service, EntityConverterFactory converterFactory) {
        this.service = service;
        this.converterFactory = converterFactory;
    }

    @Override
    public MessageResponse handle(final EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        Map<String, Object> fields = ((SearchFieldsData) entityData).getFields();
        List<AbstractEvent> events = service.findByFields(fields);
        List<AbstractEventDTO> eventsDTO = retrieveEventsDTO(events);
        return new SuccessfulMessageResponse(new MultipleEventData(eventsDTO));
    }

    private void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData)) {
            final String exceptionMessage = String.format(INVALID_SEARCH_BY_FIELDS_DATA, entityData);
            log.error(exceptionMessage);
            throw new RequestHandlerException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof SearchFieldsData);
    }

    private List<AbstractEventDTO> retrieveEventsDTO(final List<AbstractEvent> createdEvents) {
        return createdEvents.stream().map(e -> {
            EntityConverter converter = converterFactory.getConverter(e.getSourceType());
            return (AbstractEventDTO) converter.convertToDTO(e);
        }).toList();
    }
}
