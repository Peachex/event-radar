package by.klevitov.eventpersistor.messaging.handler.impl.location;

import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.EntityIdData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.INVALID_ENTITY_LOCATION_DATA;

@Log4j2
@Component
public class DeletionLocationRequestHandler implements RequestHandler {
    private final LocationService service;

    @Autowired
    public DeletionLocationRequestHandler(LocationService service) {
        this.service = service;
    }

    @Override
    public MessageResponse handle(final EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        String locationId = ((EntityIdData) entityData).getId();
        service.delete(locationId);
        return new SuccessfulMessageResponse(false);
    }

    private void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData)) {
            final String exceptionMessage = String.format(INVALID_ENTITY_LOCATION_DATA, entityData);
            log.error(exceptionMessage);
            throw new RequestHandlerException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof EntityIdData);
    }
}
