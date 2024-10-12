package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.SingleEventData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static by.klevitov.eventpersistor.messaging.handler.HandlerUtil.assertEqualsExcludingCreatedDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SingleEventCreationRequestHandlerTest {
    private static RequestHandler handler;
    private static EntityConverterFactory mockedConverterFactory;
    private static EntityConverter mockedConverter;

    @BeforeAll
    public static void init() {
        EventService mockedEventService = Mockito.mock(EventService.class);
        mockedConverterFactory = Mockito.mock(EntityConverterFactory.class);
        mockedConverter = Mockito.mock(EntityConverter.class);
        handler = new SingleEventCreationRequestHandler(mockedEventService, mockedConverterFactory);
    }

    @Test
    public void test_handle_withValidEntityData() {
        when(mockedConverterFactory.getConverter(any((Class.class))))
                .thenReturn(mockedConverter);
        when(mockedConverter.convertFromDTO(any()))
                .thenReturn(new AfishaRelaxEvent());
        when(mockedConverter.convertToDTO(any()))
                .thenReturn(new AfishaRelaxEventDTO());

        EntityData entityData = new SingleEventData(new AfishaRelaxEventDTO());
        MessageResponse expected = new SuccessfulMessageResponse(new SingleEventData(new AfishaRelaxEventDTO()));
        MessageResponse actual = handler.handle(entityData);
        assertEqualsExcludingCreatedDate(expected, actual);
    }

    @Test
    public void test_handle_withInvalidEntityData() {
        EntityData invalidEntityData = null;
        assertThrows(RequestHandlerException.class, () -> handler.handle(invalidEntityData));
    }

    @Test
    public void test_handle_withEntityDataThatContainsNullData() {
        EntityData invalidEntityData = new SingleEventData();
        assertThrows(RequestHandlerException.class, () -> handler.handle(invalidEntityData));
    }
}
