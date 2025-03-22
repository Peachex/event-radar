package by.klevitov.eventmanager.executor.impl;

import by.klevitov.eventmanager.service.EventFetcherService;
import by.klevitov.eventmanager.service.EventPersistorClientService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.EventDate;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static by.klevitov.eventradarcommon.dto.EventSourceType.AFISHA_RELAX;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RemoveExpiredEventsTaskExecutorTest {
    private RemoveExpiredEventsTaskExecutor removeExpiredEventsTaskExecutor;
    private EventFetcherService mockedFetcherService;
    private EventPersistorClientService mockedClientService;

    @BeforeEach
    public void setUp() {
        mockedFetcherService = Mockito.mock(EventFetcherService.class);
        mockedClientService = Mockito.mock(EventPersistorClientService.class);
        removeExpiredEventsTaskExecutor = new RemoveExpiredEventsTaskExecutor(mockedFetcherService, mockedClientService);
    }

    @Test
    public void test_execute() throws Exception {
        when(mockedClientService.findEvents())
                .thenReturn(createEvents());
        when(mockedFetcherService.fetch())
                .thenAnswer(invocationOnMock -> {
                    List<AbstractEventDTO> fetchedEvents = createEvents();
                    fetchedEvents.remove(0);
                    return fetchedEvents;
                });
        removeExpiredEventsTaskExecutor.execute();
        verify(mockedClientService, times(1)).findEvents();
        verify(mockedFetcherService, times(1)).fetch();
        verify(mockedClientService, times(1)).deleteEventsByIds(anyList());
    }

    private List<AbstractEventDTO> createEvents() {
        final List<AbstractEventDTO> events = new ArrayList<>();

        events.add(AfishaRelaxEventDTO.builder()
                .id("id1")
                .title("title1")
                .sourceType(AFISHA_RELAX)
                .date(null)
                .build());

        events.add(ByCardEventDTO.builder()
                .id("id2")
                .title("title2")
                .sourceType(BYCARD)
                .date(new EventDate(null, null))
                .build());

        events.add(ByCardEventDTO.builder()
                .id("id3")
                .title("title3")
                .sourceType(BYCARD)
                .date(new EventDate(null, LocalDate.now().minusDays(1)))
                .build());

        events.add(ByCardEventDTO.builder()
                .id("id4")
                .title("title4")
                .sourceType(BYCARD)
                .date(new EventDate(null, LocalDate.now().plusDays(1)))
                .build());

        events.add(ByCardEventDTO.builder()
                .id("id5")
                .title("title5")
                .sourceType(BYCARD)
                .date(new EventDate(LocalDate.now(), null))
                .build());

        events.add(ByCardEventDTO.builder()
                .id("id6")
                .title("title6")
                .sourceType(BYCARD)
                .date(new EventDate(LocalDate.now().minusYears(1), null))
                .build());

        return events;
    }

    @ParameterizedTest
    @MethodSource("events")
    public void test_findExpiredEvents(Pair<AbstractEventDTO, Integer> eventsWithExpectedValues) throws Exception {
        when(mockedFetcherService.fetch())
                .thenAnswer(invocationOnMock -> List.of(AfishaRelaxEventDTO.builder()
                        .id("id1")
                        .sourceType(AFISHA_RELAX)
                        .date(null)
                        .build()));
        Method findExpiredEventsMethod = RemoveExpiredEventsTaskExecutor.class.getDeclaredMethod(
                "findExpiredEvents", List.class);
        findExpiredEventsMethod.setAccessible(true);
        List<AbstractEventDTO> expiredEvents = (List<AbstractEventDTO>) findExpiredEventsMethod.invoke(
                removeExpiredEventsTaskExecutor, List.of(eventsWithExpectedValues.getKey()));
        assertEquals(expiredEvents.size(), eventsWithExpectedValues.getValue());
    }

    private static Stream<Pair<AbstractEventDTO, Integer>> events() {
        return Stream.of(
                Pair.of(AfishaRelaxEventDTO.builder()
                        .id("id1")
                        .sourceType(AFISHA_RELAX)
                        .date(null)
                        .build(), 0),
                Pair.of(AfishaRelaxEventDTO.builder()
                        .id("id1")
                        .sourceType(AFISHA_RELAX)
                        .date(new EventDate(null, null))
                        .build(), 1),
                Pair.of(ByCardEventDTO.builder()
                        .id("id1")
                        .sourceType(BYCARD)
                        .date(new EventDate(null, LocalDate.now().minusDays(1)))
                        .build(), 1),
                Pair.of(ByCardEventDTO.builder()
                        .id("id1")
                        .sourceType(BYCARD)
                        .date(new EventDate(null, LocalDate.now().plusDays(1)))
                        .build(), 0),
                Pair.of(ByCardEventDTO.builder()
                        .id("id1")
                        .sourceType(BYCARD)
                        .date(new EventDate(LocalDate.now(), null))
                        .build(), 0),
                Pair.of(ByCardEventDTO.builder()
                        .id("id1")
                        .sourceType(BYCARD)
                        .date(new EventDate(LocalDate.now().minusYears(1), null))
                        .build(), 1)
        );
    }

    @Test
    public void test_retrieveExecutorId() throws Exception {
        Field taskNameField = RemoveExpiredEventsTaskExecutor.class.getDeclaredField("TASK_NAME");
        taskNameField.setAccessible(true);
        String expected = (String) taskNameField.get(taskNameField);
        String actual = removeExpiredEventsTaskExecutor.retrieveExecutorId();
        assertEquals(expected, actual);
    }
}
