package by.klevitov.eventpersistor.messaging.factory;

import by.klevitov.eventpersistor.messaging.exception.RequestHandlerFactoryException;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.SearchAllEventRequestHandler;
import by.klevitov.eventradarcommon.messaging.request.EntityType;
import by.klevitov.eventradarcommon.messaging.request.RequestType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class RequestHandlerFactoryTest {
    private static RequestHandlerFactory handlerFactory;
    private static RequestHandler handler;

    @BeforeAll
    public static void init() {
        handler = Mockito.mock(SearchAllEventRequestHandler.class);
        handlerFactory = new RequestHandlerFactory(List.of(handler));
    }

    @Test
    public void test_getHandler_withExistentHandler() {
        RequestHandler expected = handler;
        RequestHandler actual = handlerFactory.getHandler(EntityType.EVENT, RequestType.SEARCH_FOR_ALL);
        assertEquals(expected, actual);
    }

    @Test
    public void test_getHandler_withNonExistentHandler() {
        assertThrowsExactly(RequestHandlerFactoryException.class,
                () -> handlerFactory.getHandler(EntityType.EVENT, RequestType.UPDATE));
    }
}
