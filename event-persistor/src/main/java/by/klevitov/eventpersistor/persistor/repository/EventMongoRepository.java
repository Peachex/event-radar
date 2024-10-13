package by.klevitov.eventpersistor.persistor.repository;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventradarcommon.dto.EventSourceType;

import java.util.Optional;

public interface EventMongoRepository extends EntityMongoRepository<AbstractEvent, String>, EventRepository {
    Optional<AbstractEvent> findFirstByTitleAndCategoryIgnoreCaseAndSourceType(String title, String category,
                                                                               EventSourceType sourceType);

    long countByLocation(Location location);
}
