package by.klevitov.eventpersistor.repository.impl;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Map;

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
        repository.findByFields(null);
        verify(mongoTemplate, never()).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withEmptyFields() {
        repository.findByFields(Map.of());
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withSimpleFields() {
        repository.findByFields(Map.of("simpleField", "value"));
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withComplexFields() {
        repository.findByFields(Map.of("location.country", "value"));
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }

    @Test
    public void test_findByFields_withBothSimpleAndComplexFields() {
        when(mongoTemplate.find(any(), any()))
                .thenReturn(List.of(new Location("67aa3d0e9c61a064b13b84c7", "country", "city")));
        repository.findByFields(Map.of("simpleField", "value1", "location.country", "value2"));
        verify(mongoTemplate, times(2)).find(any(), eq(AbstractEvent.class));
    }
}
