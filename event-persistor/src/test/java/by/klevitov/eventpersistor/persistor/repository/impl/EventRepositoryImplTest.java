package by.klevitov.eventpersistor.persistor.repository;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.repository.impl.EventRepositoryImpl;
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
    void setUp() {
        mongoTemplate = Mockito.mock(MongoTemplate.class);
        repository = new EventRepositoryImpl(mongoTemplate);
    }

    @Test
    void test_findByFields() {
        repository.findByFields(Map.of());
        verify(mongoTemplate, times(1)).find(any(), eq(AbstractEvent.class));
    }
}
