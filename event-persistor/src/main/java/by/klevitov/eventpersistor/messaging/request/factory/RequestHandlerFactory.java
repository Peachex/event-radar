package by.klevitov.eventpersistor.messaging.request.factory;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityType;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.RequestType;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.messaging.request.handler.impl.event.EventMultipleCreationRequestHandler;
import by.klevitov.eventpersistor.messaging.request.handler.impl.location.LocationMultipleCreationRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityType.EVENT;
import static by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityType.LOCATION;

@Component
public class RequestHandlerFactory {
    private final Map<EntityType, Map<RequestType, RequestHandler>> handlerMap = new HashMap<>();

    @Autowired
    public RequestHandlerFactory(List<RequestHandler> handlerList) {
        for (RequestHandler handler : handlerList) {
            if (handler instanceof EventMultipleCreationRequestHandler) {
                handlerMap.computeIfAbsent(EVENT, t -> new HashMap<>())
                        .put(RequestType.CREATE_MULTIPLE, handler);
            } else if (handler instanceof LocationMultipleCreationRequestHandler) {
                handlerMap.computeIfAbsent(LOCATION, t -> new HashMap<>())
                        .put(RequestType.CREATE_MULTIPLE, handler);
            }
        }
    }

    //todo Add other newly created handlers to the map. To add: UpdatingRequest,SearchForAllRequest, SearchByIdRequest,
    // SearchByFieldsRequest, DeletionRequest, SingleCreationRequest.

    public RequestHandler getHandler(EntityType entityType, RequestType requestType) {
        return handlerMap.get(entityType).get(requestType);
    }
}
