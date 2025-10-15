package by.klevitov.coordinateresolver.service;

import by.klevitov.eventradarcommon.dto.LocationDTO;

import java.util.List;

public interface CoordinateResolverService {
    LocationDTO resolve(final LocationDTO location);

    List<LocationDTO> resolve(final List<LocationDTO> locations);
}
