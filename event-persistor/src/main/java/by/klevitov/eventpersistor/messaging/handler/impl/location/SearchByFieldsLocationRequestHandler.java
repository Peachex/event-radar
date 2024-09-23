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
import by.klevitov.eventradarcommon.messaging.request.data.SearchFieldsData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static by.klevitov.eventpersistor.messaging.constant.MessagingExceptionMessage.INVALID_SEARCH_BY_FIELDS_DATA;


@Log4j2
@Component
public class SearchByFieldsLocationRequestHandler implements RequestHandler {
    private final LocationService service;
    private final EntityConverterFactory converterFactory;

    public SearchByFieldsLocationRequestHandler(LocationService service, EntityConverterFactory converterFactory) {
        this.service = service;
        this.converterFactory = converterFactory;
    }

    @Override
    public MessageResponse handle(EntityData entityData) {
        throwExceptionInCaseOfInvalidEntityData(entityData);
        Map<String, Object> fields = ((SearchFieldsData) entityData).getFields();
        List<Location> locations = service.findByFields(fields);
        EntityConverter converter = converterFactory.getConverter(LocationDTO.class);
        List<LocationDTO> locationsDTO = retrieveLocationsDTO(locations, converter);
        return new SuccessfulMessageResponse(new MultipleLocationData(locationsDTO));
    }

    public void throwExceptionInCaseOfInvalidEntityData(final EntityData entityData) {
        if (entityDataIsNotValid(entityData)) {
            final String exceptionMessage = String.format(INVALID_SEARCH_BY_FIELDS_DATA, entityData);
            log.error(exceptionMessage);
            throw new RequestHandlerException(exceptionMessage);
        }
    }

    private boolean entityDataIsNotValid(final EntityData entityData) {
        return !(entityData instanceof SearchFieldsData);
    }

    private List<LocationDTO> retrieveLocationsDTO(final List<Location> createdLocations,
                                                   final EntityConverter converter) {
        return createdLocations.stream().map(l -> (LocationDTO) converter.convertToDTO(l)).toList();
    }
}
