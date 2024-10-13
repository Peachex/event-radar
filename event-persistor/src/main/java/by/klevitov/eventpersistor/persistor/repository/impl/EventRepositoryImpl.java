package by.klevitov.eventpersistor.persistor.repository.impl;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.repository.EventRepository;
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
    private final MongoTemplate mongoTemplate;

    @Autowired
    public EventRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<AbstractEvent> findByFields(final Map<String, Object> fields) {
        final Query query = new Query();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            query.addCriteria(where(entry.getKey()).regex(compile(entry.getValue().toString(), CASE_INSENSITIVE)));
        }
        return mongoTemplate.find(query, AbstractEvent.class);
    }
}
