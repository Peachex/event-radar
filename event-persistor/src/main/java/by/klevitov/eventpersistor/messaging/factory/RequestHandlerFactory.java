package by.klevitov.eventpersistor.messaging.factory;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityType;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.RequestType;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.MultipleEventCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.event.SingleEventCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.MultipleLocationCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.handler.impl.location.SingleLocationCreationRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityType.EVENT;
import static by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityType.LOCATION;
import static by.klevitov.eventpersistor.messaging.comnon.request.dto.RequestType.CREATE_MULTIPLE;
import static by.klevitov.eventpersistor.messaging.comnon.request.dto.RequestType.CREATE_SINGLE;

@Component
public class RequestHandlerFactory {
    private final Map<EntityType, Map<RequestType, RequestHandler>> handlerMap = new HashMap<>();

    @Autowired
    public RequestHandlerFactory(List<RequestHandler> handlerList) {
        for (RequestHandler handler : handlerList) {
            if (handler instanceof MultipleEventCreationRequestHandler) {
                handlerMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(CREATE_MULTIPLE, handler);
            } else if (handler instanceof MultipleLocationCreationRequestHandler) {
                handlerMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(CREATE_MULTIPLE, handler);
            } else if (handler instanceof SingleEventCreationRequestHandler) {
                handlerMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(CREATE_SINGLE, handler);
            } else if (handler instanceof SingleLocationCreationRequestHandler) {
                handlerMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(CREATE_SINGLE, handler);
            }
        }
    }

    //todo Add other newly created handlers to the map. To add: UpdatingRequest,SearchForAllRequest, SearchByIdRequest,
    // SearchByFieldsRequest, DeletionRequest, SingleCreationRequest.

    public RequestHandler getHandler(EntityType entityType, RequestType requestType) {
        return handlerMap.get(entityType).get(requestType);
    }
}
