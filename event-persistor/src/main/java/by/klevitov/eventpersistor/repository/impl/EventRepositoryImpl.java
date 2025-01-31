package by.klevitov.eventpersistor.repository.impl;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.repository.EventRepository;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        //todo Refactor this method.
        // Step 1: Initialize a list to hold the matching Location objects
        List<Location> locations = new ArrayList<>();

        // Step 2: Iterate through the fields to separate Location queries from Event queries
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Handling Location fields (e.g., location.city, location.country)
            if (key.startsWith("location.")) {
                String subField = key.split("\\.")[1];  // Extract "city" or "country"
                Query locationQuery = new Query(Criteria.where(subField)
                        .regex(compile(value.toString(), CASE_INSENSITIVE)));
                locations.addAll(mongoTemplate.find(locationQuery, Location.class));

            } else {
                // Handling Event fields (e.g., title, price)
                Query eventQuery = new Query(Criteria.where(key).regex(compile(value.toString(), CASE_INSENSITIVE)));
                // Perform the search directly on the Event collection
                return mongoTemplate.find(eventQuery, AbstractEvent.class);
            }
        }

        // Step 3: Collect all the Location IDs
        Set<ObjectId> locationIds = locations.stream()
                .map(location -> new ObjectId(location.getId()))  // Ensure it's ObjectId
                .collect(Collectors.toSet());

        // Step 4: If there are Location results, query the Event collection with location.$id
        if (!locationIds.isEmpty()) {
            Query eventQuery = new Query(Criteria.where("location.$id").in(locationIds));
            return mongoTemplate.find(eventQuery, AbstractEvent.class);
        }

        // If no Location filters, return empty list or results based on Event-specific criteria
        return new ArrayList<>();
    }
}
