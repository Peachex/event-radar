package by.klevitov.eventpersistor.persistor.service;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.persistor.entity.ByCardEvent;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.exception.EventValidatorException;
import by.klevitov.eventpersistor.persistor.repository.EventMongoRepository;
import by.klevitov.eventpersistor.persistor.service.impl.EventServiceImpl;
import by.klevitov.eventpersistor.persistor.util.EventValidator;
import by.klevitov.eventradarcommon.dto.EventDate;
import by.klevitov.eventradarcommon.dto.EventPrice;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.klevitov.eventradarcommon.dto.EventSourceType.AFISHA_RELAX;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventServiceImplTest {
    private EventService eventService;
    private LocationService locationService;
    private EventMongoRepository repository;

    @BeforeEach
    void setUp() {
        locationService = Mockito.mock(LocationService.class);
        repository = Mockito.mock(EventMongoRepository.class);
        eventService = new EventServiceImpl(locationService, repository);
    }

    @Test
    public void test_create_withValidUniqueSingleEvent() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeCreation(any(AbstractEvent.class)))
                    .then(invocationOnMock -> null);
            when(locationService.create(any(Location.class)))
                    .thenReturn(new Location("id", "country", "city"));
            when(repository.findFirstByTitleAndCategoryIgnoreCaseAndSourceType(anyString(), anyString(),
                    any(EventSourceType.class)))
                    .thenReturn(Optional.empty());
            when(repository.insert(any(AbstractEvent.class)))
                    .thenReturn(createTestEvent(AFISHA_RELAX));

            AbstractEvent expected = createTestEvent(AFISHA_RELAX);
            AbstractEvent actual = eventService.create(createTestEvent(AFISHA_RELAX));

            verify(repository, times(1)).insert(any(AbstractEvent.class));
            verify(locationService, times(1)).create(any(Location.class));
            assertEquals(expected, actual);
            assertEquals(expected.getId(), actual.getId());
        }
    }

    @Test
    public void test_create_withValidNonUniqueSingleEvent() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeCreation(any(AbstractEvent.class)))
                    .then(invocationOnMock -> null);
            when(locationService.create(any(Location.class)))
                    .thenReturn(new Location("id", "country", "city"));
            when(repository.findFirstByTitleAndCategoryIgnoreCaseAndSourceType(anyString(), anyString(),
                    any(EventSourceType.class)))
                    .thenReturn(Optional.of(createTestEvent(BYCARD)));

            AbstractEvent expected = createTestEvent(BYCARD);
            AbstractEvent actual = eventService.create(createTestEvent(BYCARD));

            verify(repository, never()).insert(any(AbstractEvent.class));
            verify(locationService, times(1)).create(any(Location.class));
            assertEquals(expected, actual);
            assertEquals(expected.getId(), actual.getId());
        }
    }

    @Test
    public void test_create_withInvalidSingleEvent() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeCreation(any(AbstractEvent.class)))
                    .thenThrow(new EventValidatorException("Event title cannot be null or empty."));

            AbstractEvent event = new AfishaRelaxEvent();
            Exception exception = assertThrows(EventValidatorException.class, () -> eventService.create(event));

            String expectedMessage = "Event title cannot be null or empty.";
            String actualMessage = exception.getMessage();
            assertEquals(expectedMessage, actualMessage);
            verify(locationService, never()).create(any(Location.class));
            verify(repository, never()).insert(any(AbstractEvent.class));
        }
    }

    @Test
    public void test_create_withValidUniqueMultipleEvents() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeCreation(any(AbstractEvent.class)))
                    .then(invocationOnMock -> null);
            when(repository.findAll())
                    .thenReturn(new ArrayList<>());

            List<AbstractEvent> events = List.of(
                    AfishaRelaxEvent.builder().title("title1").dateStr("dateStr1").sourceType(AFISHA_RELAX).build(),
                    ByCardEvent.builder().title("title2").dateStr("dateStr2").sourceType(BYCARD).build(),
                    AfishaRelaxEvent.builder().title("title3").dateStr("dateStr3").sourceType(AFISHA_RELAX).build()
            );
            when(repository.saveAll(anyList()))
                    .thenReturn(List.of(
                            AfishaRelaxEvent.builder().id("id1").title(events.get(0).getTitle())
                                    .dateStr(events.get(0).getDateStr())
                                    .sourceType(events.get(0).getSourceType()).build(),
                            ByCardEvent.builder().id("id2").title(events.get(1).getTitle())
                                    .dateStr(events.get(1).getDateStr())
                                    .sourceType(events.get(1).getSourceType()).build(),
                            AfishaRelaxEvent.builder().id("id3").title(events.get(2).getTitle())
                                    .dateStr(events.get(2).getDateStr())
                                    .sourceType(events.get(2).getSourceType()).build()
                    ));

            List<AbstractEvent> expected = List.of(
                    AfishaRelaxEvent.builder().id("id1").title(events.get(0).getTitle())
                            .dateStr(events.get(0).getDateStr())
                            .sourceType(events.get(0).getSourceType()).build(),
                    ByCardEvent.builder().id("id2").title(events.get(1).getTitle())
                            .dateStr(events.get(1).getDateStr())
                            .sourceType(events.get(1).getSourceType()).build(),
                    AfishaRelaxEvent.builder().id("id3").title(events.get(2).getTitle())
                            .dateStr(events.get(2).getDateStr())
                            .sourceType(events.get(2).getSourceType()).build()
            );
            List<AbstractEvent> actual = eventService.create(events);

            verify(repository, times(1)).saveAll(anyList());
            assertEquals(expected, actual);
            verifyLocationsId(expected, actual);
        }
    }

    @Test
    public void test_create_withValidNonUniqueMultipleEvents() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeCreation(any(AbstractEvent.class)))
                    .then(invocationOnMock -> null);

            List<AbstractEvent> events = List.of(
                    AfishaRelaxEvent.builder().title("title1").dateStr("dateStr1").sourceType(AFISHA_RELAX).build(),
                    ByCardEvent.builder().title("title2").dateStr("dateStr2").sourceType(BYCARD).build(),
                    AfishaRelaxEvent.builder().title("title3").dateStr("dateStr3").sourceType(AFISHA_RELAX).build()
            );
            when(repository.findAll())
                    .thenAnswer(invocationOnMock -> {
                        List<AbstractEvent> existentEvents = new ArrayList<>();
                        existentEvents.add(AfishaRelaxEvent.builder().id("id1").title(events.get(0).getTitle()).
                                dateStr(events.get(0).getDateStr()).sourceType(events.get(0).getSourceType()).build());
                        existentEvents.add(AfishaRelaxEvent.builder().id("id3").title(events.get(2).getTitle())
                                .dateStr(events.get(2).getDateStr()).sourceType(events.get(2).getSourceType()).build());
                        return existentEvents;
                    });


            when(repository.saveAll(anyList()))
                    .thenReturn(List.of(
                            ByCardEvent.builder().id("id2").title(events.get(1).getTitle())
                                    .dateStr(events.get(1).getDateStr())
                                    .sourceType(events.get(1).getSourceType()).build()
                    ));

            List<AbstractEvent> expected = List.of(
                    AfishaRelaxEvent.builder().id("id1").title(events.get(0).getTitle())
                            .dateStr(events.get(0).getDateStr())
                            .sourceType(events.get(0).getSourceType()).build(),
                    ByCardEvent.builder().id("id2").title(events.get(1).getTitle())
                            .dateStr(events.get(1).getDateStr())
                            .sourceType(events.get(1).getSourceType()).build(),
                    AfishaRelaxEvent.builder().id("id3").title(events.get(2).getTitle())
                            .dateStr(events.get(2).getDateStr())
                            .sourceType(events.get(2).getSourceType()).build()
            );
            List<AbstractEvent> actual = eventService.create(events);

            verify(repository, times(1)).saveAll(anyList());
            assertEquals(expected, actual);
            verifyLocationsId(expected, actual);
        }
    }

    private void verifyLocationsId(final List<AbstractEvent> expected, final List<AbstractEvent> actual) {
        for (int i = 0; i < expected.size() && i < actual.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
        }
    }

    @Test
    public void test_create_withInvalidMultipleEvents() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeCreation(any(AbstractEvent.class)))
                    .thenThrow(new EventValidatorException("Event title cannot be null or empty."));

            List<AbstractEvent> events = List.of(
                    new AfishaRelaxEvent(),
                    new ByCardEvent()
            );
            Exception exception = assertThrows(EventValidatorException.class, () ->
                    eventService.create(events));

            String expectedMessage = "Event title cannot be null or empty.";
            String actualMessage = exception.getMessage();
            assertEquals(expectedMessage, actualMessage);
            verify(repository, never()).saveAll(anyList());
        }
    }

    private AbstractEvent createTestEvent(EventSourceType sourceType) {
        return switch (sourceType) {
            case AFISHA_RELAX -> AfishaRelaxEvent.builder()
                    .id("id")
                    .title("title")
                    .location(new Location("country", "city"))
                    .dateStr("dateStr")
                    .date(new EventDate(LocalDate.of(2024, 10, 8),
                            LocalDate.of(2024, 10, 11)))
                    .category("category")
                    .sourceType(AFISHA_RELAX)
                    .eventLink("eventLink")
                    .imageLink("imageLink")
                    .build();
            case BYCARD -> ByCardEvent.builder()
                    .id("id")
                    .title("title")
                    .location(new Location("country", "city"))
                    .dateStr("dateStr")
                    .date(new EventDate(LocalDate.of(2024, 10, 8),
                            LocalDate.of(2024, 10, 11)))
                    .category("category")
                    .sourceType(BYCARD)
                    .priceStr("priceStr")
                    .price(new EventPrice(new BigDecimal(10), new BigDecimal(20)))
                    .eventLink("eventLink")
                    .imageLink("imageLink")
                    .build();
        };
    }
}
