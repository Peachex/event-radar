package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleEventData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchAllEventRequestHandler implements RequestHandler {
    private final EventService service;
    private final EntityConverterFactory converterFactory;

    @Autowired
    public SearchAllEventRequestHandler(EventService service, EntityConverterFactory converterFactory) {
        this.service = service;
        this.converterFactory = converterFactory;
    }

    @Override
    public MessageResponse handle(final EntityData entityData) {
        List<AbstractEvent> events = service.findAll();
        List<AbstractEventDTO> eventsDTO = retrieveEventsDTO(events);
        return new SuccessfulMessageResponse(new MultipleEventData(eventsDTO));
    }

    private List<AbstractEventDTO> retrieveEventsDTO(final List<AbstractEvent> createdEvents) {
        return createdEvents.stream().map(e -> {
            EntityConverter converter = converterFactory.getConverter(e.getSourceType());
            return (AbstractEventDTO) converter.convertToDTO(e);
        }).toList();
    }
}
