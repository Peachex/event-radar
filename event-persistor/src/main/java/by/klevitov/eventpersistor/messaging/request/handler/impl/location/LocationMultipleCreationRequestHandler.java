package by.klevitov.eventpersistor.messaging.request.handler.impl.location;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.messaging.comnon.request.dto.data.MultipleLocationData;
import by.klevitov.eventpersistor.messaging.request.handler.RequestHandler;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationMultipleCreationRequestHandler implements RequestHandler {
    private final LocationService service;

    @Autowired
    public LocationMultipleCreationRequestHandler(LocationService service) {
        this.service = service;
    }

    @Override
    public Object handle(EntityData entityData) {
        if (entityData instanceof MultipleLocationData data) {
            List<Location> locations = data.getLocations();
            return service.create(locations);
        }
        throw new IllegalArgumentException("Invalid data for creating locations.");
    }
}
