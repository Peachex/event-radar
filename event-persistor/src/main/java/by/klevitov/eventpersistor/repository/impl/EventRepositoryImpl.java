package by.klevitov.eventpersistor.repository.impl;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.repository.EventRepository;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class EventRepositoryImpl implements EventRepository {
    private static final String TITLE_FIELD_NAME = "title";
    private static final String CATEGORY_FIELD_NAME = "category";
    private static final String SOURCE_TYPE_FIELD_NAME = "sourceType";
    private final MongoTemplate mongoTemplate;

    @Autowired
    public EventRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<AbstractEvent> findFirstByTitleAndCategoryIgnoreCaseAndSourceType(final List<AbstractEvent> events) {
        List<String> titles = events.stream().map(AbstractEvent::getTitle).toList();
        List<String> categories = events.stream().map(AbstractEvent::getCategory).toList();
        List<EventSourceType> sourceTypes = events.stream().map(AbstractEvent::getSourceType).toList();
        final Query query = new Query();
        query.addCriteria(
                where(TITLE_FIELD_NAME)
                        .in(titles)
                        .and(CATEGORY_FIELD_NAME)
                        .in(categories)
                        .and(SOURCE_TYPE_FIELD_NAME)
                        .in(sourceTypes));
        return mongoTemplate.find(query, AbstractEvent.class);
    }

    public List<AbstractEvent> findByFields(final Map<String, Object> fields) {
        final Query query = new Query();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            query.addCriteria(where(entry.getKey()).regex(compile(entry.getValue().toString(), CASE_INSENSITIVE)));
        }
        return mongoTemplate.find(query, AbstractEvent.class);
    }
}
