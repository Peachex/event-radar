package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.EntityIdData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeletionEventRequestHandlerTest {
    private static RequestHandler handler;
    private static EventService eventService;

    @BeforeAll
    public static void init() {
        eventService = Mockito.mock(EventService.class);
        handler = new DeletionEventRequestHandler(eventService);
    }

    @Test
    public void test_handle_withValidEntityData() {
//        when(eventService.delete(anyString()))
//                .thenAnswer(invocationOnMock ->null);
        EntityData entityData = new EntityIdData("id");
        MessageResponse expected = new SuccessfulMessageResponse(false);
        MessageResponse actual = handler.handle(entityData);
        assertEquals(expected, actual);
    }
}
