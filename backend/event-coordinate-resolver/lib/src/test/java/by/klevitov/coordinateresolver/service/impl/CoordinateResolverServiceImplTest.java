package by.klevitov.coordinateresolver.service.impl;

import by.klevitov.coordinateresolver.client.GeocodingClient;
import by.klevitov.coordinateresolver.exception.CoordinateResolverServiceException;
import by.klevitov.coordinateresolver.model.GeoCoordinates;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class CoordinateResolverServiceImplTest {
    private GeocodingClient mockedGeocodingClient;
    private CoordinateResolverServiceImpl service;

    @BeforeEach
    void setUp() {
        mockedGeocodingClient = mock(GeocodingClient.class);
        service = new CoordinateResolverServiceImpl(mockedGeocodingClient);
    }

    @Test
    void resolve_shouldReturnNull_whenLocationNull() {
        assertNull(service.resolve((LocationDTO) null));
    }

    @Test
    void resolve_shouldReturnSameLocation_whenRawAddressBlank() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("  ");
        assertSame(location, service.resolve(location));
        verifyNoInteractions(mockedGeocodingClient);
    }

    @Test
    void resolve_shouldApplyCoordinates_whenClientReturnsCoordinates() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("rawAddress");
        location.setCity("city");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.of(new GeoCoordinates(12.3, 45.6789)));

        LocationDTO locationsWithCoordinates = service.withDelay(0).resolve(location);

        assertEquals(12.3, locationsWithCoordinates.getLatitude());
        assertEquals(45.6789, locationsWithCoordinates.getLongitude());
        verify(mockedGeocodingClient).fetchCoordinates(anyString());
    }

    @Test
    void resolve_shouldNotApplyCoordinates_whenClientReturnsEmpty() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("rawAddress");
        location.setCity("city");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.empty());

        LocationDTO locationsWithCoordinates = service.withDelay(0).resolve(location);

        assertEquals(0.0, locationsWithCoordinates.getLatitude());
        assertEquals(0.0, locationsWithCoordinates.getLongitude());
    }

    @Test
    void withDelay_shouldThrow_whenNegativeDelay() {
        assertThrows(CoordinateResolverServiceException.class, () -> service.withDelay(-1));
    }

    @Test
    void withDelay_shouldReturnSelf_whenValidDelay() {
        CoordinateResolverServiceImpl result = service.withDelay(500);
        assertSame(service, result);
    }

    @Test
    void resolve_shouldProcessListOfLocations() {
        LocationDTO location1 = new LocationDTO();
        location1.setRawAddress("rawAddress1");

        LocationDTO location2 = new LocationDTO();
        location2.setRawAddress("rawAddress2");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.empty());

        List<LocationDTO> locations = List.of(location1, location2);
        List<LocationDTO> locationsWithCoordinates = service.withDelay(0).resolve(locations);

        assertEquals(2, locationsWithCoordinates.size());
        verify(mockedGeocodingClient, times(2)).fetchCoordinates(anyString());
    }

    @Test
    void resolve_shouldRespectDelay() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("rawAddress");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.of(new GeoCoordinates(1.0, 2.0)));

        long delayInMillis = 200L;
        service.withDelay(delayInMillis);

        long start = System.nanoTime();
        service.resolve(location);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        assertTrue(elapsedMs >= delayInMillis - 10,
                "Expected at least " + delayInMillis + "ms delay, but got " + elapsedMs + "ms");
    }

    @Test
    void normalizeAddress_shouldRemovePrefixesAndAppendCity() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("г. Минск, ул. Ленина, кв. 3");
        location.setCity("Минск");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.empty());

        service.withDelay(0).resolve(location);

        verify(mockedGeocodingClient).fetchCoordinates(argThat(address ->
                address.toLowerCase().contains("улица") &&
                        address.startsWith("Минск") &&
                        !address.contains("кв.")
        ));
    }

    @Test
    void normalizeAddress_shouldExpandCommonAbbreviations() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("г. Минск, пр-т Победителей, пер. Солнечный, пл. Ленина");
        location.setCity("Минск");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.empty());

        service.withDelay(0).resolve(location);

        verify(mockedGeocodingClient).fetchCoordinates(argThat(address ->
                address.contains("проспект") &&
                        address.contains("переулок") &&
                        address.contains("площадь") &&
                        !address.contains("пр-т") &&
                        !address.contains("пер.") &&
                        !address.contains("пл.")
        ));
    }

    @Test
    void normalizeAddress_shouldRemoveApartmentAndOfficeInformation() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("г. Минск, ул. Ленина, офис 12, этаж 3, помещение 7");
        location.setCity("Минск");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.empty());

        service.withDelay(0).resolve(location);

        verify(mockedGeocodingClient).fetchCoordinates(argThat(address ->
                !address.matches(".*(офис|этаж|помещение).*")
        ));
    }

    @Test
    void normalizeAddress_shouldTrimAndRemoveExtraCommasAndSpaces() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("  Минск,,,   ул. Ленина   ,  5  ");
        location.setCity("Минск");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.empty());

        service.withDelay(0).resolve(location);

        verify(mockedGeocodingClient).fetchCoordinates(argThat(address ->
                !address.contains(",") && !address.contains("  ") && address.startsWith("Минск")
        ));
    }

    @Test
    void normalizeAddress_shouldPrependCityIfMissing() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("ул. Победителей 10");
        location.setCity("Минск");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.empty());

        service.withDelay(0).resolve(location);

        verify(mockedGeocodingClient).fetchCoordinates(argThat(address ->
                address.startsWith("Минск") &&
                        address.toLowerCase().contains("улица победителей")
        ));
    }

    @Test
    void normalizeAddress_shouldNotDuplicateCityIfAlreadyPresent() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("Минск улица Победителей 10");
        location.setCity("Минск");

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.empty());

        service.withDelay(0).resolve(location);

        verify(mockedGeocodingClient).fetchCoordinates(argThat(address ->
                address.equals("Минск улица Победителей 10")
        ));
    }

    @Test
    void normalizeAddress_shouldHandleNullCityGracefully() {
        LocationDTO location = new LocationDTO();
        location.setRawAddress("ул. Победителей 10");
        location.setCity(null);

        when(mockedGeocodingClient.fetchCoordinates(anyString()))
                .thenReturn(Optional.empty());

        service.withDelay(0).resolve(location);

        verify(mockedGeocodingClient).fetchCoordinates(argThat(address ->
                address.startsWith("улица") && !address.startsWith("null")
        ));
    }
}
