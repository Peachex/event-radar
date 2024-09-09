package by.klevitov.eventpersistor.messaging.request.handler.impl.location;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.data.MultipleLocationData;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class LocationMultipleCreationRequestHandler implements RequestHandler {
    private final LocationService service;

    @Autowired
    public LocationMultipleCreationRequestHandler(LocationService service) {
        this.service = service;
    }

    @Override
    public MessageResponse handle(EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        MultipleLocationData eventData = (MultipleLocationData) entityData;
        List<Location> locationsToCreate = eventData.getLocations();
        List<Location> createdLocations = service.create(locationsToCreate);
        return new MessageResponse("responseId", new MultipleLocationData(createdLocations));
    }

    private void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData)) {
            final String exceptionMessage = "Invalid data for creating locations: " + entityData;
            log.error(exceptionMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof MultipleLocationData);
    }
}
