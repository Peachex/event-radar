package by.klevitov.eventpersistor.repository.impl;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.repository.EventRepository;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
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
        final Query locationQuery = new Query();
        final Query eventQueryForSimpleSearch = new Query();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            //todo Replace location. with pattern regex.
            if (key.startsWith("location.")) {
                locationQuery.addCriteria(createCriteriaForLocationQuery(key, value));
            } else {
                eventQueryForSimpleSearch.addCriteria(Criteria.where(key).regex(compile(value, CASE_INSENSITIVE)));
            }
        }
        List<Location> locations = mongoTemplate.find(locationQuery, Location.class);
        Set<ObjectId> locationIds = extractIdsFromLocations(locations);
        List<AbstractEvent> eventsFromComplexFieldSearch = findEventsUsingComplexFieldSearchQuery(locationIds);
        List<AbstractEvent> eventsFromSimpleFieldSearch = findEventsUsingSimpleFieldSearchQuery(eventQueryForSimpleSearch);
        return (List<AbstractEvent>) CollectionUtils.intersection(eventsFromSimpleFieldSearch, eventsFromComplexFieldSearch);
    }

    private Criteria createCriteriaForLocationQuery(final String key, final String value) {
        String subField = key.split("\\.")[1];
        return Criteria.where(subField).regex(compile(value, CASE_INSENSITIVE));
    }

    private Set<ObjectId> extractIdsFromLocations(final List<Location> locations) {
        return locations.stream()
                .map(location -> new ObjectId(location.getId()))
                .collect(Collectors.toSet());
    }

    private List<AbstractEvent> findEventsUsingComplexFieldSearchQuery(Set<ObjectId> locationIds) {
        if (isEmpty(locationIds)) {
            return new ArrayList<>();
        }
        //todo Replace string with something constant.
        final Query eventQueryForComplexFieldSearch = new Query(Criteria.where("location.$id").in(locationIds));
        return new ArrayList<>(mongoTemplate.find(eventQueryForComplexFieldSearch, AbstractEvent.class));
    }

    private List<AbstractEvent> findEventsUsingSimpleFieldSearchQuery(final Query eventQueryForSimpleFieldSearch) {
        return new ArrayList<>(mongoTemplate.find(eventQueryForSimpleFieldSearch, AbstractEvent.class));
    }
}
