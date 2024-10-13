package by.klevitov.eventpersistor.messaging.handler.impl.location;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.RequestHandlerException;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.SingleLocationData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.INVALID_ENTITY_LOCATION_DATA;

@Log4j2
@Component
public class SingleLocationCreationRequestHandler implements RequestHandler {
    private final LocationService service;
    private final EntityConverterFactory converterFactory;

    @Autowired
    public SingleLocationCreationRequestHandler(LocationService service, EntityConverterFactory converterFactory) {
        this.service = service;
        this.converterFactory = converterFactory;
    }

    @Override
    public MessageResponse handle(final EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        LocationDTO locationDTO = ((SingleLocationData) entityData).getLocationDTO();
        EntityConverter converter = converterFactory.getConverter(locationDTO.getClass());
        Location locationToCreate = (Location) converter.convertFromDTO(locationDTO);
        Location createdLocation = service.create(locationToCreate);
        LocationDTO createdLocationDTO = (LocationDTO) converter.convertToDTO(createdLocation);
        return new SuccessfulMessageResponse(new SingleLocationData(createdLocationDTO));
    }

    private void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData) || entityDataContainsNullData(entityData)) {
            final String exceptionMessage = String.format(INVALID_ENTITY_LOCATION_DATA, entityData);
            log.error(exceptionMessage);
            throw new RequestHandlerException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof SingleLocationData);
    }

    private boolean entityDataContainsNullData(final EntityData entityData) {
        return ((SingleLocationData) entityData).getLocationDTO() == null;
    }
}
