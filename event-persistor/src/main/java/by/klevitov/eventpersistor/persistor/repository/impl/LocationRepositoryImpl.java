package by.klevitov.eventpersistor.persistor.repository.impl;

import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.repository.LocationRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class LocationRepositoryImpl implements LocationRepository {
    private final MongoTemplate mongoTemplate;

    public LocationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Location> findByFields(Map<String, Object> fields) {
        final Query query = new Query();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            query.addCriteria(where(entry.getKey()).regex(compile(entry.getValue().toString(), CASE_INSENSITIVE)));
        }
        return mongoTemplate.find(query, Location.class);
    }
}
