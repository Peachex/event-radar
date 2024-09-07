package by.klevitov.eventpersistor.messaging.service.impl;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityType;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.MessageRequest;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.RequestType;
import by.klevitov.eventpersistor.messaging.request.factory.RequestHandlerFactory;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageServiceImpl implements MessageService {
    private final RequestHandlerFactory handlerFactory;

    @Autowired
    public MessageServiceImpl(RequestHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    @Override
    public Object processAndRetrieveResult(MessageRequest messageRequest) {
        RequestType requestType = messageRequest.getRequestType();
        EntityType entityType = messageRequest.getEntityType();
        EntityData entityData = messageRequest.getEntityData();

        RequestHandler handler = handlerFactory.getHandler(entityType, requestType);
        if (handler == null) {
            throw new IllegalArgumentException("Unsupported request/entity type: " + requestType + " for " + entityType);
        }

        return handler.handle(entityData);
    }

    //todo Add error handling and replace exception.
}
