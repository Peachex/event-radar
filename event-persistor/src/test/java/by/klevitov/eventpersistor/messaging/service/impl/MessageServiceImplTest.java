package by.klevitov.eventpersistor.messaging.service.impl;

import by.klevitov.eventpersistor.messaging.exception.MessageServiceException;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.factory.RequestHandlerFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.SearchAllLocationRequestHandler;
import by.klevitov.eventpersistor.messaging.service.MessageService;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.messaging.request.EntityType;
import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.request.RequestType;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleLocationData;
import by.klevitov.eventradarcommon.messaging.response.ErrorMessageResponse;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static by.klevitov.eventpersistor.messaging.service.MessageServiceUtil.assertEqualsExcludingCreatedDateAndRequestId;
import static by.klevitov.eventpersistor.messaging.service.MessageServiceUtil.assertEqualsExcludingCreatedDateAndRequestIdAndThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MessageServiceImplTest {
    private static RequestHandlerFactory handlerFactory;
    private static RequestHandler handler;
    private static MessageService messageService;

    @BeforeAll
    public static void init() {
        handlerFactory = Mockito.mock(RequestHandlerFactory.class);
        EntityConverterFactory converterFactory = Mockito.mock(EntityConverterFactory.class);
        LocationService locationService = Mockito.mock(LocationService.class);
        handler = new SearchAllLocationRequestHandler(locationService, converterFactory);
        messageService = new MessageServiceImpl(handlerFactory);
    }

    @Test
    public void test_processAndRetrieveResult_withExistentHandler() {
        when(handlerFactory.getHandler(any(EntityType.class), any(RequestType.class)))
                .thenReturn(handler);

        MessageRequest request = MessageRequest.builder()
                .requestType(RequestType.SEARCH_FOR_ALL)
                .entityType(EntityType.LOCATION)
                .build();

        MessageResponse expected = new SuccessfulMessageResponse(new MultipleLocationData(new ArrayList<>()));
        MessageResponse actual = messageService.processAndRetrieveResult(request);
        assertEqualsExcludingCreatedDateAndRequestId(expected, actual);
    }

    @Test
    public void test_processAndRetrieveResult_withNonExistentHandler() {
        when(handlerFactory.getHandler(any(EntityType.class), any(RequestType.class)))
                .thenReturn(null);

        MessageRequest request = new MessageRequest();

        MessageResponse expected = new ErrorMessageResponse("Unsupported request or entity type: null | null",
                new MessageServiceException("Unsupported request or entity type: null | null"));
        MessageResponse actual = messageService.processAndRetrieveResult(request);
        assertEqualsExcludingCreatedDateAndRequestIdAndThrowable(expected, actual);
    }
}
