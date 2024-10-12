package by.klevitov.eventpersistor.messaging.handler.impl.location;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.SingleLocationData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static by.klevitov.eventpersistor.messaging.handler.HandlerUtil.assertEqualsExcludingCreatedDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UpdatingLocationRequestHandlerTest {
    private static RequestHandler handler;
    private static LocationService mockedLocationService;
    private static EntityConverterFactory mockedConverterFactory;
    private static EntityConverter mockedConverter;

    @BeforeAll
    public static void init() {
        mockedLocationService = Mockito.mock(LocationService.class);
        mockedConverterFactory = Mockito.mock(EntityConverterFactory.class);
        mockedConverter = Mockito.mock(EntityConverter.class);
        handler = new UpdatingLocationRequestHandler(mockedLocationService, mockedConverterFactory);
    }

    @Test
    public void test_handle_withValidEntityData() {
        when(mockedConverterFactory.getConverter(any((Class.class))))
                .thenReturn(mockedConverter);
        when(mockedConverter.convertFromDTO(any()))
                .thenReturn(new Location());
        when(mockedConverter.convertToDTO(any()))
                .thenReturn(new LocationDTO());

        EntityData entityData = new SingleLocationData(new LocationDTO());
        MessageResponse expected = new SuccessfulMessageResponse(new SingleLocationData(new LocationDTO()));
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
        EntityData invalidEntityData = new SingleLocationData();
        assertThrows(RequestHandlerException.class, () -> handler.handle(invalidEntityData));
    }
}
