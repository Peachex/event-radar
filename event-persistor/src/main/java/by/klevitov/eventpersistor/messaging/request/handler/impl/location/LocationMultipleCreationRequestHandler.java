package by.klevitov.eventpersistor.messaging.request.handler.impl.location;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.data.MultipleLocationData;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.ErrorMessageResponse;
import by.klevitov.eventpersistor.messaging.comnon.response.dto.MessageResponse;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.exception.AbstractServiceException;
import by.klevitov.eventpersistor.persistor.exception.LocationValidatorException;
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
        if (entityDataIsNotValid(entityData)) {
            Exception e = new IllegalArgumentException("Invalid data for creating locations.");
            return createErrorResponseAndLogException(e, "Exception during request handling: {}");
        }
        MultipleLocationData eventData = (MultipleLocationData) entityData;
        return processLocationsCreationAndCreateResponse(eventData);
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof MultipleLocationData);
    }

    private ErrorMessageResponse createErrorResponseAndLogException(final Exception e, final String exceptionMessage) {
        log.error(exceptionMessage, e.getMessage());
        return new ErrorMessageResponse("responseId", e.getMessage(), e);
    }

    private MessageResponse processLocationsCreationAndCreateResponse(final MultipleLocationData eventData) {
        MessageResponse response;
        try {
            List<Location> locationsToCreate = eventData.getLocations();
            List<Location> createdLocations = service.create(locationsToCreate);
            response = new MessageResponse("responseId", new MultipleLocationData(createdLocations));
        } catch (AbstractServiceException | LocationValidatorException e) {
            response = createErrorResponseAndLogException(e, "Exception during request handling: {}");
        }
        return response;
    }
}
