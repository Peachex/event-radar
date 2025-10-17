package by.klevitov.coordinateresolver.factory;

import by.klevitov.coordinateresolver.client.GeocodingClient;
import by.klevitov.coordinateresolver.service.CoordinateResolverService;
import by.klevitov.coordinateresolver.service.impl.CoordinateResolverServiceImpl;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class CoordinateResolverFactoryTest {
    @Test
    void defaultService_shouldCreateServiceWithDefaultClient() {
        CoordinateResolverService service = CoordinateResolverFactory.defaultService();
        assertNotNull(service);
        assertInstanceOf(CoordinateResolverServiceImpl.class, service);
    }

    @Test
    void withHttpClient_shouldCreateServiceWithProvidedHttpClient() {
        OkHttpClient mockedHttpClient = mock(OkHttpClient.class);
        CoordinateResolverService service = CoordinateResolverFactory.withHttpClient(mockedHttpClient);
        assertNotNull(service);
        assertInstanceOf(CoordinateResolverServiceImpl.class, service);
    }

    @Test
    void withGeocodingClient_shouldCreateServiceWithProvidedGeocodingClient() {
        GeocodingClient mockedGeocodingClient = mock(GeocodingClient.class);
        CoordinateResolverService service = CoordinateResolverFactory.withGeocodingClient(mockedGeocodingClient);
        assertNotNull(service);
        assertInstanceOf(CoordinateResolverServiceImpl.class, service);
    }
}
