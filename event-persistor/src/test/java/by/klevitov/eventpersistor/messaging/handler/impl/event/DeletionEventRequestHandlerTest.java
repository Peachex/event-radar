package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.EntityIdData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static by.klevitov.eventpersistor.messaging.handler.RequestHandlerUtil.assertEqualsExcludingCreatedDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeletionEventRequestHandlerTest {
    private static RequestHandler handler;

    @BeforeAll
    public static void init() {
        EventService mockedEventService = Mockito.mock(EventService.class);
        handler = new DeletionEventRequestHandler(mockedEventService);
    }

    @Test
    public void test_handle_withValidEntityData() {
        EntityData entityData = new EntityIdData("id");
        MessageResponse expected = new SuccessfulMessageResponse(false);
        MessageResponse actual = handler.handle(entityData);
        assertEqualsExcludingCreatedDate(expected, actual);
    }

    @Test
    public void test_handle_withInvalidEntityData() {
        EntityData invalidEntityData = null;
        assertThrows(RequestHandlerException.class, () -> handler.handle(invalidEntityData));
    }
}
