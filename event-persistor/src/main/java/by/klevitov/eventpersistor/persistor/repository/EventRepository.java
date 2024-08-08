package by.klevitov.eventpersistor.persistor.repository;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<AbstractEvent, String> {
}
