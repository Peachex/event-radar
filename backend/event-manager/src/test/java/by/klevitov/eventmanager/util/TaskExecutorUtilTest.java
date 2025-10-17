package by.klevitov.eventmanager.util;

import by.klevitov.coordinateresolver.service.CoordinateResolverService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TaskExecutorUtilTest {
    private CoordinateResolverService mockedCoordinateResolver;

    @BeforeEach
    public void setUp() {
        mockedCoordinateResolver = Mockito.mock(CoordinateResolverService.class);
    }

    @Test
    public void test_resolveCoordinatesIfNeeded_shouldResolveWhenZeroCoordinates() {
        LocationDTO location1 = createLocation(0.0, 0.0);
        LocationDTO location2 = createLocation(10.0, 20.0);
        List<AbstractEventDTO> events = List.of(createDefaultEventWithLocation(location1), createDefaultEventWithLocation(location2));

        TaskExecutorUtil.resolveCoordinatesIfNeeded(events, mockedCoordinateResolver);

        verify(mockedCoordinateResolver, times(1)).resolve(location1);
        verifyNoMoreInteractions(mockedCoordinateResolver);
    }

    @Test
    public void test_resolveCoordinatesIfNeeded_shouldSkipNullList() {
        TaskExecutorUtil.resolveCoordinatesIfNeeded(null, mockedCoordinateResolver);
        verifyNoInteractions(mockedCoordinateResolver);
    }

    @Test
    public void test_resolveCoordinatesIfNeeded_shouldSkipEmptyList() {
        TaskExecutorUtil.resolveCoordinatesIfNeeded(List.of(), mockedCoordinateResolver);
        verifyNoInteractions(mockedCoordinateResolver);
    }

    @Test
    public void test_resolveCoordinatesIfNeeded_shouldSkipEventsWithNonZeroCoordinates() {
        List<AbstractEventDTO> events = List.of(
                createDefaultEventWithLocation(createLocation(123.4, 27.56)),
                createDefaultEventWithLocation(createLocation(567.8, -74.0))
        );
        TaskExecutorUtil.resolveCoordinatesIfNeeded(events, mockedCoordinateResolver);
        verifyNoInteractions(mockedCoordinateResolver);
    }

    @Test
    public void test_resolveCoordinatesIfNeeded_shouldSkipNullLocations() {
        AbstractEventDTO eventWithNullLocation = createDefaultEventWithLocation(null);
        List<AbstractEventDTO> events = List.of(eventWithNullLocation);

        TaskExecutorUtil.resolveCoordinatesIfNeeded(events, mockedCoordinateResolver);
        verifyNoInteractions(mockedCoordinateResolver);
    }

    @Test
    public void test_resolveCoordinatesIfNeeded_shouldResolveMultipleEligibleEvents() {
        List<AbstractEventDTO> events = List.of(
                createDefaultEventWithLocation(createLocation(0, 0)),
                createDefaultEventWithLocation(createLocation(0, 0))
        );
        TaskExecutorUtil.resolveCoordinatesIfNeeded(events, mockedCoordinateResolver);
        verify(mockedCoordinateResolver, times(2)).resolve(any(LocationDTO.class));
        verifyNoMoreInteractions(mockedCoordinateResolver);
    }

    private LocationDTO createLocation(double lat, double lon) {
        LocationDTO location = new LocationDTO();
        location.setLatitude(lat);
        location.setLongitude(lon);
        return location;
    }

    private AbstractEventDTO createDefaultEventWithLocation(LocationDTO location) {
        AbstractEventDTO event = new ByCardEventDTO();
        event.setLocation(location);
        return event;
    }
}
