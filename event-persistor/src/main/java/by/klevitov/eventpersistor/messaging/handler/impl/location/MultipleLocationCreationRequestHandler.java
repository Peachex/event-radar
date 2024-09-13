package by.klevitov.eventpersistor.messaging.handler.impl.location;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleLocationData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.INVALID_ENTITY_LOCATIONS_DATA;

@Log4j2
@Component
public class MultipleLocationCreationRequestHandler implements RequestHandler {
    private final LocationService service;
    private final EntityConverterFactory converterFactory;

    @Autowired
    public MultipleLocationCreationRequestHandler(LocationService service, EntityConverterFactory converterFactory) {
        this.service = service;
        this.converterFactory = converterFactory;
    }

    @Override
    public MessageResponse handle(final EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        List<LocationDTO> locationsDTO = ((MultipleLocationData) entityData).getLocationsDTO();
        EntityConverter converter = converterFactory.getConverter(LocationDTO.class);
        List<Location> locationsToCreate = retrieveLocationsToCreate(locationsDTO, converter);
        List<Location> createdLocations = service.create(locationsToCreate);
        List<LocationDTO> createdLocationsDTO = retrieveCreatedLocationsDTO(createdLocations, converter);
        return new SuccessfulMessageResponse(new MultipleLocationData(createdLocationsDTO));
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

    private List<Location> retrieveLocationsToCreate(final List<LocationDTO> locationsDTO,
                                                     final EntityConverter converter) {
        return locationsDTO.stream().map(l -> (Location) converter.convertFromDTO(l)).toList();
    }

    private List<LocationDTO> retrieveCreatedLocationsDTO(final List<Location> createdLocations,
                                                          final EntityConverter converter) {
        return createdLocations.stream().map(l -> (LocationDTO) converter.convertToDTO(l)).toList();
    }
}
