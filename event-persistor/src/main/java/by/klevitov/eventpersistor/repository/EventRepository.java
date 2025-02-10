package by.klevitov.eventpersistor.repository;

import by.klevitov.eventpersistor.entity.AbstractEvent;

import java.util.List;
import java.util.Map;

public interface EventRepository {
    List<AbstractEvent> findFirstByTitleAndCategoryIgnoreCaseAndSourceType(List<AbstractEvent> events);

    List<AbstractEvent> findByFields(Map<String, Object> fields, boolean isCombinedMatch);
}
