package by.klevitov.eventpersistor.messaging.service.impl;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityType;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.MessageRequest;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.RequestType;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.ErrorMessageResponse;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;
import by.klevitov.eventpersistor.messaging.request.factory.RequestHandlerFactory;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.messaging.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MessageServiceImpl implements MessageService {
    private final RequestHandlerFactory handlerFactory;

    @Autowired
    public MessageServiceImpl(RequestHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    @Override
    public MessageResponse processAndRetrieveResult(final MessageRequest request) {
        RequestType requestType = request.getRequestType();
        EntityType entityType = request.getEntityType();
        RequestHandler handler = handlerFactory.getHandler(entityType, requestType);
        return processRequest(handler, request);
    }

    private MessageResponse processRequest(final RequestHandler handler, final MessageRequest request) {
        return (handler != null
                ? processRequestForNotNullHandler(handler, request)
                : processRequestForNullHandler(request));
    }

    private MessageResponse processRequestForNotNullHandler(final RequestHandler handler, final MessageRequest request) {
        MessageResponse response;
        try {
            response = handler.handle(request.getEntityData());
        } catch (Exception e) {
            response = new ErrorMessageResponse("responseId", e.getMessage(), e);
        }
        updateResponseWithMetadata(request, response);
        return response;
    }

    private MessageResponse processRequestForNullHandler(final MessageRequest request) {
        Exception e = new IllegalArgumentException("Unsupported request/entity type: " + request.getRequestType()
                + " for " + request.getEntityType());
        log.error(e.getMessage());
        ErrorMessageResponse errorResponse = new ErrorMessageResponse("responseId", e.getMessage(), e);
        updateResponseWithMetadata(request, errorResponse);
        return errorResponse;
    }

    private void updateResponseWithMetadata(final MessageRequest request, final MessageResponse response) {
        response.setRequestId(request.getId());
        response.setRequestCreatedDate(request.getCreatedDate());
    }

    //todo Replace exception.
}
