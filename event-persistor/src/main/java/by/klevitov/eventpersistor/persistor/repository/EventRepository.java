package by.klevitov.eventpersistor.persistor.repository;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventRepository extends MongoRepository<AbstractEvent, String> {
    Optional<AbstractEvent> findByTitleAndSourceTypeIgnoreCase(String title, EventSourceType sourceType);
}
