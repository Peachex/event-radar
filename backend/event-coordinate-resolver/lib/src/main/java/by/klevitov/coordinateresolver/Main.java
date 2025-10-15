package by.klevitov.coordinateresolver;

import by.klevitov.coordinateresolver.service.CoordinateResolverService;
import by.klevitov.coordinateresolver.service.impl.CoordinateResolverServiceImpl;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;

public class Main {
    public static void main(String[] args) {
        //todo: Delete this temp class.

        AbstractEventDTO event = new ByCardEventDTO();
        LocationDTO eventLocation = new LocationDTO();
        eventLocation.setRawAddress("Минск, ул. Мясникова 44");
        eventLocation.setCity("Минск");
        event.setLocation(eventLocation);

        CoordinateResolverService service = new CoordinateResolverServiceImpl();
        service.resolve(event.getLocation());

        System.out.println(event);
    }
}
