package by.klevitov.eventpersistor.messaging.factory;

import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.MultipleEventCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.SingleEventCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.MultipleLocationCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.SingleLocationCreationRequestHandler;
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

@Component
public class RequestHandlerFactory {
    private final Map<EntityType, Map<RequestType, RequestHandler>> handlersMap;

    @Autowired
    public RequestHandlerFactory(List<RequestHandler> handlers) {
        this.handlersMap = new HashMap<>();
        for (RequestHandler handler : handlers) {
            if (handler instanceof MultipleEventCreationRequestHandler) {
                handlersMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(CREATE_MULTIPLE, handler);
            } else if (handler instanceof MultipleLocationCreationRequestHandler) {
                handlersMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(CREATE_MULTIPLE, handler);
            } else if (handler instanceof SingleEventCreationRequestHandler) {
                handlersMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(CREATE_SINGLE, handler);
            } else if (handler instanceof SingleLocationCreationRequestHandler) {
                handlersMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(CREATE_SINGLE, handler);
            }
        }
    }

    //todo Add other newly created handlers to the map. To add: UpdatingRequest,SearchForAllRequest, SearchByIdRequest,
    // SearchByFieldsRequest, DeletionRequest, SingleCreationRequest.

    public RequestHandler getHandler(EntityType entityType, RequestType requestType) {
        return handlersMap.get(entityType).get(requestType);
    }
}
