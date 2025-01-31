package by.klevitov.eventpersistor.repository.impl;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.repository.EventRepository;
import by.klevitov.eventpersistor.repository.impl.EventRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EventRepositoryImplTest {
    private MongoTemplate mongoTemplate;
    private EventRepository repository;

    @BeforeEach
    public void setUp() {
        mongoTemplate = Mockito.mock(MongoTemplate.class);
        repository = new EventRepositoryImpl(mongoTemplate);
    }

    @Test
    public void test_findByFields() {
        //fixme Fix this test after updating findByFields method.
        repository.findByFields(Map.of());
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }
}
