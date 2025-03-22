package by.klevitov.eventpersistor.repository.impl;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.entity.ByCardEvent;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventRepositoryImplTest {
    private MongoTemplate mongoTemplate;
    private EventRepository repository;

    @BeforeEach
    public void setUp() {
        mongoTemplate = Mockito.mock(MongoTemplate.class);
        repository = new EventRepositoryImpl(mongoTemplate);
    }

    @Test
    public void test_findByFields_withNullFields() {
        repository.findByFields(null, false);
        verify(mongoTemplate, never()).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withEmptyFields() {
        repository.findByFields(Map.of(), false);
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withSimpleFields_withCombinedMatch_withoutPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder().title("title1").build();
        AbstractEvent byCardEvent = ByCardEvent.builder().title("title2").build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent, byCardEvent));

        List<AbstractEvent> expected = List.of(afishaEvent, byCardEvent);
        List<AbstractEvent> actual = repository.findByFields(Map.of("title", "title"), true);
        assertEquals(expected, actual);
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withSimpleFields_withCombinedMatch_withPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder().title("title1").build();
        AbstractEvent byCardEvent = ByCardEvent.builder().title("title2").build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent, byCardEvent));

        Page<AbstractEvent> expected = new PageImpl<>(List.of(afishaEvent), Pageable.ofSize(1), 2);
        Page<AbstractEvent> actual = repository.findByFields(Map.of("title", "title"), true,
                PageRequest.of(0, 1));
        assertEquals(expected, actual);
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withSimpleFields_withoutCombinedMatch_withoutPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder().title("title1").build();
        AbstractEvent byCardEvent = ByCardEvent.builder().title("title2").build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent, byCardEvent));

        List<AbstractEvent> expected = List.of(afishaEvent, byCardEvent);
        List<AbstractEvent> actual = repository.findByFields(Map.of("title", "title"), false);
        assertEquals(expected, actual);
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withSimpleFields_withoutCombinedMatch_withPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder().title("title1").build();
        AbstractEvent byCardEvent = ByCardEvent.builder().title("title2").build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent, byCardEvent));

        Page<AbstractEvent> expected = new PageImpl<>(List.of(afishaEvent), Pageable.ofSize(1), 2);
        Page<AbstractEvent> actual = repository.findByFields(Map.of("title", "title"), false,
                PageRequest.of(0, 1));
        assertEquals(expected, actual);
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withComplexFields_withCombinedMatch_withoutPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder()
                .location(new Location("67aa3d0e9c61a064b13b84c7", "countryValue", "cityValue"))
                .build();

        ByCardEvent byCardEvent = ByCardEvent.builder()
                .location(new Location("11aa3d0e9c61a064b13b84c1", "countryValue", "cityValue"))
                .build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent.getLocation(), byCardEvent.getLocation()))
                .thenReturn(List.of(afishaEvent, byCardEvent))
                .thenReturn(new ArrayList<>());

        List<AbstractEvent> expected = new ArrayList<>();
        List<AbstractEvent> actual = repository.findByFields(
                Map.of("location.country", "cityValue", "location.city", "cityValue"), true);
        assertEquals(expected, actual);
        verify(mongoTemplate, times(2)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withComplexFields_withCombinedMatch_withPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder()
                .location(new Location("67aa3d0e9c61a064b13b84c7", "countryValue", "cityValue"))
                .build();

        ByCardEvent byCardEvent = ByCardEvent.builder()
                .location(new Location("11aa3d0e9c61a064b13b84c1", "countryValue", "cityValue"))
                .build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent.getLocation(), byCardEvent.getLocation()))
                .thenReturn(List.of(afishaEvent, byCardEvent))
                .thenReturn(new ArrayList<>());

        Page<AbstractEvent> expected = new PageImpl<>(new ArrayList<>(), Pageable.ofSize(1), 0);
        Page<AbstractEvent> actual = repository.findByFields(
                Map.of("location.country", "cityValue", "location.city", "cityValue"), true,
                PageRequest.of(0, 1));
        assertEquals(expected, actual);
        verify(mongoTemplate, times(2)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withComplexFields_withoutCombinedMatch_withoutPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder()
                .location(new Location("67aa3d0e9c61a064b13b84c7", "countryValue", "cityValue"))
                .build();

        ByCardEvent byCardEvent = ByCardEvent.builder()
                .location(new Location("11aa3d0e9c61a064b13b84c1", "countryValue", "cityValue"))
                .build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent.getLocation(), byCardEvent.getLocation()))
                .thenReturn(List.of(afishaEvent, byCardEvent))
                .thenReturn(new ArrayList<>());

        List<AbstractEvent> expected = List.of(afishaEvent, byCardEvent);
        List<AbstractEvent> actual = repository.findByFields(
                Map.of("location.country", "cityValue", "location.city", "cityValue"), false);
        assertEquals(expected, actual);
        verify(mongoTemplate, times(2)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withComplexFields_withoutCombinedMatch_withPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder()
                .location(new Location("67aa3d0e9c61a064b13b84c7", "countryValue", "cityValue"))
                .build();

        ByCardEvent byCardEvent = ByCardEvent.builder()
                .location(new Location("11aa3d0e9c61a064b13b84c1", "countryValue", "cityValue"))
                .build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent.getLocation(), byCardEvent.getLocation()))
                .thenReturn(List.of(afishaEvent, byCardEvent))
                .thenReturn(new ArrayList<>());

        Page<AbstractEvent> expected = new PageImpl<>(List.of(afishaEvent), Pageable.ofSize(1), 2);
        Page<AbstractEvent> actual = repository.findByFields(
                Map.of("location.country", "cityValue", "location.city", "cityValue"), false,
                PageRequest.of(0, 1));
        assertEquals(expected, actual);
        verify(mongoTemplate, times(2)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withBothSimpleAndComplexFieldsAndCombinedMatch_withoutPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder()
                .title("titleValue")
                .location(new Location("67aa3d0e9c61a064b13b84c7", "countryValue", "cityValue"))
                .build();

        ByCardEvent byCardEvent = ByCardEvent.builder()
                .title("anotherUniqueTitleValue")
                .location(new Location("11aa3d0e9c61a064b13b84c1", "countryValue", "cityValue"))
                .build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent.getLocation(), byCardEvent.getLocation()))
                .thenReturn(List.of(afishaEvent))
                .thenReturn(List.of(byCardEvent));

        List<AbstractEvent> expected = new ArrayList<>();
        List<AbstractEvent> actual = repository.findByFields(
                Map.of("title", "titleValue", "location.country", "countryValue"), true);
        assertEquals(expected, actual);
        verify(mongoTemplate, times(2)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withBothSimpleAndComplexFieldsAndCombinedMatch_withPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder()
                .title("titleValue")
                .location(new Location("67aa3d0e9c61a064b13b84c7", "countryValue", "cityValue"))
                .build();

        ByCardEvent byCardEvent = ByCardEvent.builder()
                .title("anotherUniqueTitleValue")
                .location(new Location("11aa3d0e9c61a064b13b84c1", "countryValue", "cityValue"))
                .build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent.getLocation(), byCardEvent.getLocation()))
                .thenReturn(List.of(afishaEvent))
                .thenReturn(List.of(byCardEvent));

        Page<AbstractEvent> expected = new PageImpl<>(new ArrayList<>(), Pageable.ofSize(1), 0);
        Page<AbstractEvent> actual = repository.findByFields(
                Map.of("title", "titleValue", "location.country", "countryValue"), true,
                PageRequest.of(0, 1));
        assertEquals(expected, actual);
        verify(mongoTemplate, times(2)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withBothSimpleAndComplexFieldsAndWithoutCombinedMatch_withoutPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder()
                .title("titleValue")
                .location(new Location("67aa3d0e9c61a064b13b84c7", "countryValue", "cityValue"))
                .build();

        ByCardEvent byCardEvent = ByCardEvent.builder()
                .title("anotherUniqueTitleValue")
                .location(new Location("11aa3d0e9c61a064b13b84c1", "countryValue", "cityValue"))
                .build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent.getLocation(), byCardEvent.getLocation()))
                .thenReturn(List.of(afishaEvent))
                .thenReturn(List.of(byCardEvent));

        List<AbstractEvent> expected = List.of(byCardEvent, afishaEvent);
        List<AbstractEvent> actual = repository.findByFields(
                Map.of("title", "titleValue", "location.country", "countryValue"), false);
        assertEquals(expected, actual);
        verify(mongoTemplate, times(2)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withBothSimpleAndComplexFieldsAndWithoutCombinedMatch_withPagination() {
        AbstractEvent afishaEvent = AfishaRelaxEvent.builder()
                .title("titleValue")
                .location(new Location("67aa3d0e9c61a064b13b84c7", "countryValue", "cityValue"))
                .build();

        ByCardEvent byCardEvent = ByCardEvent.builder()
                .title("anotherUniqueTitleValue")
                .location(new Location("11aa3d0e9c61a064b13b84c1", "countryValue", "cityValue"))
                .build();

        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(afishaEvent.getLocation(), byCardEvent.getLocation()))
                .thenReturn(List.of(afishaEvent))
                .thenReturn(List.of(byCardEvent));

        Page<AbstractEvent> expected = new PageImpl<>(List.of(byCardEvent), Pageable.ofSize(1), 2);
        Page<AbstractEvent> actual = repository.findByFields(
                Map.of("title", "titleValue", "location.country", "countryValue"), false,
                PageRequest.of(0, 1));
        assertEquals(expected, actual);
        verify(mongoTemplate, times(2)).find(any(), eq(AbstractEvent.class));
    }
}
