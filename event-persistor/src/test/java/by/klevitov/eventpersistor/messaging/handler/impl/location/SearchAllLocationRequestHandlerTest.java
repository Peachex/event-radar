package by.klevitov.eventpersistor.messaging.handler.impl.location;

import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleLocationData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static by.klevitov.eventpersistor.messaging.handler.HandlerUtil.assertEqualsExcludingCreatedDate;

public class SearchAllLocationRequestHandlerTest {
    private static RequestHandler handler;
    private static LocationService mockedLocationService;
    private static EntityConverterFactory mockedConverterFactory;

    @BeforeAll
    public static void init() {
        mockedLocationService = Mockito.mock(LocationService.class);
        mockedConverterFactory = Mockito.mock(EntityConverterFactory.class);
        handler = new SearchAllLocationRequestHandler(mockedLocationService, mockedConverterFactory);
    }

    @Test
    public void test_handle_withValidEntityData() {
        MessageResponse expected = new SuccessfulMessageResponse(new MultipleLocationData(new ArrayList<>()));
        MessageResponse actual = handler.handle(null);
        assertEqualsExcludingCreatedDate(expected, actual);
    }
}
