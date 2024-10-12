package by.klevitov.eventpersistor.messaging.handler.impl.event;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.EntityIdData;
import by.klevitov.eventradarcommon.messaging.request.data.SingleEventData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static by.klevitov.eventpersistor.messaging.handler.RequestHandlerUtil.assertEqualsExcludingCreatedDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SearchByIdEventRequestHandlerTest {
    private static RequestHandler handler;
    private static EventService mockedEventService;
    private static EntityConverterFactory mockedConverterFactory;
    private static EntityConverter mockedConverter;

    @BeforeAll
    public static void init() {
        mockedEventService = Mockito.mock(EventService.class);
        mockedConverterFactory = Mockito.mock(EntityConverterFactory.class);
        mockedConverter = Mockito.mock(EntityConverter.class);
        handler = new SearchByIdEventRequestHandler(mockedEventService, mockedConverterFactory);
    }

    @Test
    public void test_handle_withValidEntityData() {
        when(mockedEventService.findById(anyString()))
                .thenReturn(AfishaRelaxEvent.builder().sourceType(EventSourceType.AFISHA_RELAX).build());
        when(mockedConverterFactory.getConverter(any(EventSourceType.class)))
                .thenReturn(mockedConverter);
        when(mockedConverter.convertToDTO(any()))
                .thenReturn(new AfishaRelaxEventDTO());

        EntityData entityData = new EntityIdData("id");
        MessageResponse expected = new SuccessfulMessageResponse(new SingleEventData(new AfishaRelaxEventDTO()));
        MessageResponse actual = handler.handle(entityData);
        assertEqualsExcludingCreatedDate(expected, actual);
    }

    @Test
    public void test_handle_withInvalidEntityData() {
        EntityData invalidEntityData = null;
        assertThrows(RequestHandlerException.class, () -> handler.handle(invalidEntityData));
    }
}
