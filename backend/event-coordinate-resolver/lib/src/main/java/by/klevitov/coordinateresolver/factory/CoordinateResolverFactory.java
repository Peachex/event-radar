package by.klevitov.coordinateresolver.factory;

import by.klevitov.coordinateresolver.client.GeocodingClient;
import by.klevitov.coordinateresolver.client.impl.OpenStreetMapGeocodingClient;
import by.klevitov.coordinateresolver.service.CoordinateResolverService;
import by.klevitov.coordinateresolver.service.impl.CoordinateResolverServiceImpl;
import okhttp3.OkHttpClient;

public final class CoordinateResolverFactory {
    private CoordinateResolverFactory() {
    }

    public static CoordinateResolverService defaultService() {
        return new CoordinateResolverServiceImpl(new OpenStreetMapGeocodingClient());
    }

    public static CoordinateResolverService withHttpClient(OkHttpClient httpClient) {
        return new CoordinateResolverServiceImpl(new OpenStreetMapGeocodingClient(httpClient));
    }

    public static CoordinateResolverService withGeocodingClient(GeocodingClient client) {
        return new CoordinateResolverServiceImpl(client);
    }
}
