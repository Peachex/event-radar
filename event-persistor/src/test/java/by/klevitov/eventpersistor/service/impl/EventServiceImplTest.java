package by.klevitov.eventpersistor.service.impl;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.entity.ByCardEvent;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.exception.EventAlreadyExistsException;
import by.klevitov.eventpersistor.exception.EventNotFoundException;
import by.klevitov.eventpersistor.exception.EventValidatorException;
import by.klevitov.eventpersistor.repository.EventMongoRepository;
import by.klevitov.eventpersistor.service.EventService;
import by.klevitov.eventpersistor.service.LocationService;
import by.klevitov.eventpersistor.util.EventValidator;
import by.klevitov.eventradarcommon.dto.EventDate;
import by.klevitov.eventradarcommon.dto.EventPrice;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.eventradarcommon.pagination.util.PageRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.klevitov.eventradarcommon.dto.EventSourceType.AFISHA_RELAX;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventServiceImplTest {
    private EventService eventService;
    private LocationService locationService;
    private EventMongoRepository repository;

    @BeforeEach
    public void setUp() {
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
            assertThrows(EventValidatorException.class, () -> eventService.create(event));

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
            verifyEventsId(expected, actual);
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
            when(repository.findFirstByTitleAndCategoryIgnoreCaseAndSourceType(anyList()))
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
            verifyEventsId(expected, actual);
        }
    }

    private void verifyEventsId(final List<AbstractEvent> expected, final List<AbstractEvent> actual) {
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
            assertThrows(EventValidatorException.class, () -> eventService.create(events));

            verify(repository, never()).saveAll(anyList());
        }
    }

    @Test
    public void test_findById_withValidIdAndExistentEvent() {
        when(repository.findById(anyString()))
                .thenReturn(Optional.of(createTestEvent(AFISHA_RELAX)));
        AbstractEvent expected = createTestEvent(AFISHA_RELAX);
        AbstractEvent actual = eventService.findById("id");
        assertEquals(expected, actual);
    }

    @Test
    public void test_findById_withValidIdAndNonExistentEvent() {
        when(repository.findById(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> eventService.findById("id"));
    }

    @Test
    public void test_findById_withInvalidId() {
        assertThrows(EventValidatorException.class, () -> eventService.findById(null));
    }

    @Test
    public void test_findByFields_withExistentFields_withoutPagination() {
        when(repository.findByFields(anyMap(), anyBoolean()))
                .thenReturn(List.of(createTestEvent(AFISHA_RELAX), createTestEvent(BYCARD)));
        List<AbstractEvent> expected = List.of(createTestEvent(AFISHA_RELAX), createTestEvent(BYCARD));
        List<AbstractEvent> actual = eventService.findByFields(Map.of("existentField", "fieldValue"), false);
        assertEquals(expected, actual);
    }

    @Test
    public void test_findByFields_withExistentFields_withPagination() {
        try (MockedStatic<PageRequestValidator> validator = Mockito.mockStatic(PageRequestValidator.class)) {
            validator.when(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)))
                    .then(invocationOnMock -> null);
            when(repository.findByFields(anyMap(), anyBoolean(), any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(List.of(
                            createTestEvent(AFISHA_RELAX), createTestEvent(BYCARD)
                    ), Pageable.ofSize(3), 3));
            Page<AbstractEvent> expected = new PageImpl<>(List.of(
                    createTestEvent(AFISHA_RELAX), createTestEvent(BYCARD)
            ), Pageable.ofSize(3), 3);
            Page<AbstractEvent> actual = eventService.findByFields(Map.of("existentField", "fieldValue"), false,
                    new PageRequestDTO(0, 3, null));
            assertEquals(expected, actual);
            validator.verify(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)));
        }
    }

    @Test
    public void test_findByFields_withNonExistentFields_withoutPagination() {
        when(repository.findByFields(anyMap(), anyBoolean())).thenReturn(new ArrayList<>());
        List<AbstractEvent> actual = eventService.findByFields(Map.of("nonExistentField", "fieldValue"), false);
        assertEquals(0, actual.size());
    }

    @Test
    public void test_findByFields_withNonExistentFields_withPagination() {
        try (MockedStatic<PageRequestValidator> validator = Mockito.mockStatic(PageRequestValidator.class)) {
            validator.when(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)))
                    .then(invocationOnMock -> null);
            when(repository.findByFields(anyMap(), anyBoolean(), any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(new ArrayList<>(), Pageable.ofSize(3), 3));
            Page<AbstractEvent> expected = new PageImpl<>(new ArrayList<>(), Pageable.ofSize(3), 3);
            Page<AbstractEvent> actual = eventService.findByFields(Map.of("nonExistentField", "fieldValue"), false,
                    new PageRequestDTO(0, 3, null));
            assertEquals(expected, actual);
            validator.verify(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)));
        }
    }

    @Test
    public void test_findAll_withoutPagination() {
        when(repository.findAll())
                .thenReturn(List.of(createTestEvent(AFISHA_RELAX), createTestEvent(BYCARD)));
        List<AbstractEvent> expected = List.of(createTestEvent(AFISHA_RELAX), createTestEvent(BYCARD));
        List<AbstractEvent> actual = eventService.findAll();
        assertEquals(expected, actual);
        verifyEventsId(expected, actual);
    }

    @Test
    public void test_findAll_withPagination() {
        try (MockedStatic<PageRequestValidator> validator = Mockito.mockStatic(PageRequestValidator.class)) {
            validator.when(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)))
                    .then(invocationOnMock -> null);
            when(repository.findAll(any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(List.of(
                            createTestEvent(AFISHA_RELAX),
                            createTestEvent(BYCARD)
                    ), Pageable.ofSize(2), 2));
            Page<AbstractEvent> expected = new PageImpl<>(List.of(
                    createTestEvent(AFISHA_RELAX),
                    createTestEvent(BYCARD)
            ), Pageable.ofSize(2), 2);
            Page<AbstractEvent> actual = eventService.findAll(new PageRequestDTO(0, 2, null));
            assertEquals(expected, actual);
            verifyEventsId(expected.getContent(), actual.getContent());
            validator.verify(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)));
        }
    }

    @Test
    public void test_update_withValidExistentEvent() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeUpdating(any(AbstractEvent.class)))
                    .then(invocationOnMock -> null);
            when(repository.findById(anyString()))
                    .thenReturn(Optional.of(createTestEvent(AFISHA_RELAX)));
            when(repository.findFirstByTitleAndCategoryIgnoreCaseAndSourceType(anyString(), anyString(),
                    any(EventSourceType.class)))
                    .thenReturn(Optional.empty());

            String updatedTitle = "Updated title";
            AbstractEvent updatedEvent = createTestEvent(AFISHA_RELAX);
            updatedEvent.setTitle(updatedTitle);
            when(repository.save(updatedEvent))
                    .thenAnswer(invocationOnMock ->
                    {
                        AbstractEvent event = createTestEvent(AFISHA_RELAX);
                        event.setTitle(updatedTitle);
                        return event;
                    });

            AbstractEvent expected = createTestEvent(AFISHA_RELAX);
            expected.setTitle(updatedTitle);
            AbstractEvent actual = eventService.update(updatedEvent);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void test_update_withValidNonExistentEvent() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeUpdating(any(AbstractEvent.class)))
                    .then(invocationOnMock -> null);
            when(repository.findById(anyString()))
                    .thenReturn(Optional.empty());

            AbstractEvent updatedEvent = createTestEvent(AFISHA_RELAX);
            updatedEvent.setId("nonExistentEventId");
            assertThrows(EventNotFoundException.class, () -> eventService.update(updatedEvent));

            verify(repository, times(1)).findById(anyString());
            verify(repository, never()).save(any());
            verify(repository, never()).findFirstByTitleAndCategoryIgnoreCaseAndSourceType(anyString(), anyString(),
                    any(EventSourceType.class));
        }
    }

    @Test
    public void test_update_withInvalidEvent() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeUpdating(any(AbstractEvent.class)))
                    .thenThrow(new EventValidatorException("Event id cannot be null or empty."));

            AbstractEvent updatedEvent = AfishaRelaxEvent.builder().id(null).build();
            assertThrows(EventValidatorException.class, () -> eventService.update(updatedEvent));

            verify(repository, never()).findById(anyString());
            verify(repository, never()).save(any());
            verify(repository, never()).findFirstByTitleAndCategoryIgnoreCaseAndSourceType(anyString(), anyString(),
                    any(EventSourceType.class));
        }
    }

    @Test
    public void test_update_withValidEventThatAlreadyExistsAfterUpdating() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.validateEventBeforeUpdating(any(AbstractEvent.class)))
                    .then(invocationOnMock -> null);
            when(repository.findById(anyString()))
                    .thenReturn(Optional.of(createTestEvent(BYCARD)));

            String updatedTitle = "Updated title";
            when(repository.findFirstByTitleAndCategoryIgnoreCaseAndSourceType(anyString(), anyString(),
                    any(EventSourceType.class)))
                    .thenAnswer(invocationOnMock -> {
                        AbstractEvent event = createTestEvent(BYCARD);
                        event.setId("other_id");
                        event.setTitle(updatedTitle);
                        return Optional.of(event);
                    });

            AbstractEvent updatedEvent = createTestEvent(BYCARD);
            updatedEvent.setTitle(updatedTitle);

            assertThrows(EventAlreadyExistsException.class, () -> eventService.update(updatedEvent));

            verify(repository, times(1)).findById(anyString());
            verify(repository, never()).save(any());
            verify(repository, times(1)).findFirstByTitleAndCategoryIgnoreCaseAndSourceType(
                    anyString(), anyString(), any(EventSourceType.class));
        }
    }

    @Test
    public void test_delete_withValidExistentEventId() {
        when(repository.findById(anyString()))
                .thenReturn(Optional.of(createTestEvent(BYCARD)));
        eventService.delete("id");
        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).deleteById(anyString());
    }

    @Test
    public void test_delete_withInvalidEventId() {
        try (MockedStatic<EventValidator> validator = Mockito.mockStatic(EventValidator.class)) {
            validator.when(() -> EventValidator.throwExceptionInCaseOfEmptyId(nullable(String.class)))
                    .thenThrow(new EventValidatorException("Event id cannot be null or empty."));

            assertThrows(EventValidatorException.class, () -> eventService.delete(null));

            verify(repository, never()).findById(anyString());
            verify(repository, never()).deleteById(anyString());
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
