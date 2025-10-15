package by.klevitov.coordinateresolver.client;

import by.klevitov.coordinateresolver.model.GeoCoordinates;

import java.util.Optional;

public interface GeocodingClient {
    Optional<GeoCoordinates> fetchCoordinates(final String rawAddress);
}
