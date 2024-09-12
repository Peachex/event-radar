package by.klevitov.eventpersistor.messaging.request.handler.impl.location;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.data.MultipleLocationData;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.SuccessfulMessageResponse;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.INVALID_ENTITY_LOCATIONS_DATA;

@Log4j2
@Component
public class MultipleLocationCreationRequestHandler implements RequestHandler {
    private final LocationService service;

    @Autowired
    public MultipleLocationCreationRequestHandler(LocationService service) {
        this.service = service;
    }

    @Override
    public MessageResponse handle(EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        MultipleLocationData locationsData = (MultipleLocationData) entityData;
        List<Location> locationsToCreate = locationsData.getLocationsDTO();
        List<Location> createdLocations = service.create(locationsToCreate);
        return new SuccessfulMessageResponse(new MultipleLocationData(createdLocations));
    }

    private void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData)) {
            final String exceptionMessage = String.format(INVALID_ENTITY_LOCATIONS_DATA, entityData);
            log.error(exceptionMessage);
            throw new RequestHandlerException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof MultipleLocationData);
    }
}
