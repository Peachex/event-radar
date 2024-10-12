package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleEventData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static by.klevitov.eventpersistor.messaging.handler.HandlerUtil.assertEqualsExcludingCreatedDate;

public class SearchAllEventRequestHandlerTest {
    private static RequestHandler handler;

    @BeforeAll
    public static void init() {
        EventService mockedEventService = Mockito.mock(EventService.class);
        EntityConverterFactory mockedConverterFactory = Mockito.mock(EntityConverterFactory.class);
        handler = new SearchAllEventRequestHandler(mockedEventService, mockedConverterFactory);
    }

    @Test
    public void test_handle_withValidEntityData() {
        MessageResponse expected = new SuccessfulMessageResponse(new MultipleEventData(new ArrayList<>()));
        MessageResponse actual = handler.handle(null);
        assertEqualsExcludingCreatedDate(expected, actual);
    }
}
