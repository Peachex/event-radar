package by.klevitov.eventpersistor.messaging.factory;

import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.DeletionEventRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.MultipleEventCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.SearchAllEventRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.SearchByFieldsEventRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.SearchByIdEventRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.SingleEventCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.UpdatingEventRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.DeletionLocationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.MultipleLocationCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.SearchAllLocationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.SearchByFieldsLocationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.SearchByIdLocationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.SingleLocationCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.UpdatingLocationRequestHandler;
import by.klevitov.eventradarcommon.messaging.request.EntityType;
import by.klevitov.eventradarcommon.messaging.request.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.klevitov.eventradarcommon.messaging.request.EntityType.EVENT;
import static by.klevitov.eventradarcommon.messaging.request.EntityType.LOCATION;
import static by.klevitov.eventradarcommon.messaging.request.RequestType.CREATE_MULTIPLE;
import static by.klevitov.eventradarcommon.messaging.request.RequestType.CREATE_SINGLE;
import static by.klevitov.eventradarcommon.messaging.request.RequestType.DELETE;
import static by.klevitov.eventradarcommon.messaging.request.RequestType.SEARCH_BY_FIELDS;
import static by.klevitov.eventradarcommon.messaging.request.RequestType.SEARCH_BY_ID;
import static by.klevitov.eventradarcommon.messaging.request.RequestType.SEARCH_FOR_ALL;
import static by.klevitov.eventradarcommon.messaging.request.RequestType.UPDATE;

@Component
public class RequestHandlerFactory {
    private final Map<EntityType, Map<RequestType, RequestHandler>> handlersMap;

    @Autowired
    public RequestHandlerFactory(List<RequestHandler> handlers) {
        this.handlersMap = new HashMap<>();
        for (RequestHandler handler : handlers) {
            if (handler instanceof SingleEventCreationRequestHandler) {
                handlersMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(CREATE_SINGLE, handler);
            } else if (handler instanceof MultipleEventCreationRequestHandler) {
                handlersMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(CREATE_MULTIPLE, handler);
            } else if (handler instanceof SearchByIdEventRequestHandler) {
                handlersMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(SEARCH_BY_ID, handler);
            } else if (handler instanceof SearchByFieldsEventRequestHandler) {
                handlersMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(SEARCH_BY_FIELDS, handler);
            } else if (handler instanceof SearchAllEventRequestHandler) {
                handlersMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(SEARCH_FOR_ALL, handler);
            } else if (handler instanceof UpdatingEventRequestHandler) {
                handlersMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(UPDATE, handler);
            } else if (handler instanceof DeletionEventRequestHandler) {
                handlersMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(DELETE, handler);
            } else if (handler instanceof SingleLocationCreationRequestHandler) {
                handlersMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(CREATE_SINGLE, handler);
            } else if (handler instanceof MultipleLocationCreationRequestHandler) {
                handlersMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(CREATE_MULTIPLE, handler);
            } else if (handler instanceof SearchByIdLocationRequestHandler) {
                handlersMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(SEARCH_BY_ID, handler);
            } else if (handler instanceof SearchByFieldsLocationRequestHandler) {
                handlersMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(SEARCH_BY_FIELDS, handler);
            } else if (handler instanceof SearchAllLocationRequestHandler) {
                handlersMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(SEARCH_FOR_ALL, handler);
            } else if (handler instanceof UpdatingLocationRequestHandler) {
                handlersMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(UPDATE, handler);
            } else if (handler instanceof DeletionLocationRequestHandler) {
                handlersMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(DELETE, handler);
            }
        }
    }

    public RequestHandler getHandler(EntityType entityType, RequestType requestType) {
        return handlersMap.get(entityType).get(requestType);
    }
}
