package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleEventData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static by.klevitov.eventpersistor.messaging.handler.HandlerUtil.assertEqualsExcludingCreatedDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MultipleEventCreationRequestHandlerTest {
    private static RequestHandler handler;
    private static EventService eventService;
    private static EntityConverterFactory converterFactory;

    @BeforeAll
    public static void init() {
        eventService = Mockito.mock(EventService.class);
        converterFactory = Mockito.mock(EntityConverterFactory.class);
        handler = new MultipleEventCreationRequestHandler(eventService, converterFactory);
    }

    @Test
    public void test_handle_withValidEntityData() {
        EntityData entityData = new MultipleEventData(new ArrayList<>());
        MessageResponse expected = new SuccessfulMessageResponse(new MultipleEventData(new ArrayList<>()));
        MessageResponse actual = handler.handle(entityData);
        assertEqualsExcludingCreatedDate(expected, actual);
    }

    @Test
    public void test_handle_withInvalidEntityData() {
        EntityData invalidEntityData = null;
        assertThrows(RequestHandlerException.class, () -> handler.handle(invalidEntityData));
    }
}
