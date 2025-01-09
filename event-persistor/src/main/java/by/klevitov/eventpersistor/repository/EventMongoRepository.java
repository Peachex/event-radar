package by.klevitov.eventpersistor.repository;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventradarcommon.dto.EventSourceType;

import java.util.Optional;

public interface EventMongoRepository extends EntityMongoRepository<AbstractEvent, String>, EventRepository {
    Optional<AbstractEvent> findFirstByTitleAndCategoryIgnoreCaseAndSourceType(String title, String category,
                                                                               EventSourceType sourceType);

    long countByLocation(Location location);
}
