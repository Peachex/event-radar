package by.klevitov.eventpersistor.messaging.handler.impl.location;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.messaging.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleLocationData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import by.klevitov.eventradarcommon.messaging.response.SuccessfulMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchAllLocationRequestHandler implements RequestHandler {
    private final LocationService service;
    private final EntityConverterFactory converterFactory;

    @Autowired
    public SearchAllLocationRequestHandler(LocationService service, EntityConverterFactory converterFactory) {
        this.service = service;
        this.converterFactory = converterFactory;
    }

    @Override
    public MessageResponse handle(final EntityData entityData) {
        List<Location> locations = service.findAll();
        EntityConverter converter = converterFactory.getConverter(LocationDTO.class);
        List<LocationDTO> locationsDTO = retrieveLocationsDTO(locations, converter);
        return new SuccessfulMessageResponse(new MultipleLocationData(locationsDTO));
    }


    private List<LocationDTO> retrieveLocationsDTO(final List<Location> createdLocations,
                                                   final EntityConverter converter) {
        return createdLocations.stream().map(l -> (LocationDTO) converter.convertToDTO(l)).toList();
    }
}
