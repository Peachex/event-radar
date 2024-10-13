package by.klevitov.eventpersistor.persistor.repository.impl;

import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LocationRepositoryImplTest {
    private MongoTemplate mongoTemplate;
    private LocationRepository repository;

    @BeforeEach
    public void setUp() {
        mongoTemplate = Mockito.mock(MongoTemplate.class);
        repository = new LocationRepositoryImpl(mongoTemplate);
    }

    @Test
    public void test_findByFields() {
        repository.findByFields(Map.of());
        verify(mongoTemplate, times(1)).find(any(), eq(Location.class));
    }
}
