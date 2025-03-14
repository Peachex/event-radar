package by.klevitov.eventpersistor.repository;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public interface EventRepository {
    List<AbstractEvent> findFirstByTitleAndCategoryIgnoreCaseAndSourceType(List<AbstractEvent> events);

    List<AbstractEvent> findByFields(Map<String, Object> fields, boolean isCombinedMatch);

    Page<AbstractEvent> findByFields(Map<String, Object> fields, boolean isCombinedMatch, PageRequest pageRequest);
}
