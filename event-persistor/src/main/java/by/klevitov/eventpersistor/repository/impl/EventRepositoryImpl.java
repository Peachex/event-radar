package by.klevitov.eventpersistor.repository.impl;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.repository.EventRepository;
import by.klevitov.eventradarcommon.dto.EventSourceType;
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
import static org.apache.commons.collections4.CollectionUtils.intersection;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.CollectionUtils.union;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class EventRepositoryImpl implements EventRepository {
    private static final String TITLE_FIELD_NAME = "title";
    private static final String CATEGORY_FIELD_NAME = "category";
    private static final String SOURCE_TYPE_FIELD_NAME = "sourceType";
    private static final String LOCATION_PREFIX = "location.";
    private static final String COMPLEX_FIELD_SPLIT_REGEX = "\\.";
    private static final String MONGO_ID = "$id";
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

    @Override
    public List<AbstractEvent> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch) {
        if (fields == null) {
            return new ArrayList<>();
        }

        final Query locationQuery = new Query();
        final Query eventQueryForSimpleSearch = new Query();
        processCriteriaAdditions(fields, isCombinedMatch, locationQuery, eventQueryForSimpleSearch);

        List<Location> locations = findLocationsByQuery(locationQuery);
        Set<ObjectId> locationIds = extractIdsFromLocations(locations);
        final List<AbstractEvent> eventsFromComplexFieldSearch = findEventsUsingComplexFieldSearchQuery(locationIds);

        final List<AbstractEvent> eventsFromSimpleFieldSearch = findEventsUsingSimpleFieldSearchQuery(eventQueryForSimpleSearch);

        return isCombinedMatch
                ? (List<AbstractEvent>) intersection(eventsFromComplexFieldSearch, eventsFromSimpleFieldSearch)
                : (List<AbstractEvent>) union(eventsFromComplexFieldSearch, eventsFromSimpleFieldSearch);
    }

    private void processCriteriaAdditions(final Map<String, Object> fields, final boolean isCombinedMatch,
                                          final Query locationQuery, final Query eventQueryForSimpleSearch) {
        final List<Criteria> criteriaListForLocationQuery = new ArrayList<>();
        final List<Criteria> criteriaListForSimpleFieldSearchQuery = new ArrayList<>();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            final String fieldName = entry.getKey();
            final String fieldValue = (String) entry.getValue();
            if (searchFieldIsComplex(fieldName)) {
                criteriaListForLocationQuery.add(createCriteriaForLocationQuery(fieldName, fieldValue));
            } else {
                criteriaListForSimpleFieldSearchQuery.add(createCriteriaForSimpleFieldSearchQuery(fieldName, fieldValue));
            }
        }
        addCriteriaToQuery(locationQuery, criteriaListForLocationQuery, isCombinedMatch);
        addCriteriaToQuery(eventQueryForSimpleSearch, criteriaListForSimpleFieldSearchQuery, isCombinedMatch);
    }

    private boolean searchFieldIsComplex(final String field) {
        return field.startsWith(LOCATION_PREFIX);
    }

    private Criteria createCriteriaForLocationQuery(final String fieldName, final String fieldValue) {
        String subField = fieldName.split(COMPLEX_FIELD_SPLIT_REGEX)[1];
        return Criteria.where(subField).regex(compile(fieldValue, CASE_INSENSITIVE));
    }

    private Criteria createCriteriaForSimpleFieldSearchQuery(final String fieldName, final String fieldValue) {
        return Criteria.where(fieldName).regex(compile(fieldValue, CASE_INSENSITIVE));
    }

    private void addCriteriaToQuery(final Query query, final List<Criteria> criteriaList,
                                    final boolean isCombinedMatch) {
        if (isNotEmpty(criteriaList)) {
            query.addCriteria(isCombinedMatch
                    ? new Criteria().andOperator(criteriaList)
                    : new Criteria().orOperator(criteriaList));
        }
    }

    private List<Location> findLocationsByQuery(final Query query) {
        final List<Location> locations = new ArrayList<>();
        if (queryHasCriteria(query)) {
            locations.addAll(mongoTemplate.find(query, Location.class));
        }
        return locations;
    }

    private boolean queryHasCriteria(final Query query) {
        return (!query.getQueryObject().isEmpty());
    }

    private Set<ObjectId> extractIdsFromLocations(final List<Location> locations) {
        return locations.stream()
                .map(location -> new ObjectId(location.getId()))
                .collect(Collectors.toSet());
    }

    private List<AbstractEvent> findEventsUsingComplexFieldSearchQuery(final Set<ObjectId> locationIds) {
        List<AbstractEvent> events = new ArrayList<>();
        if (isNotEmpty(locationIds)) {
            final Query eventQuery = new Query(createCriteriaForComplexFieldSearchQuery(locationIds));
            events.addAll(mongoTemplate.find(eventQuery, AbstractEvent.class));
        }
        return events;
    }

    private Criteria createCriteriaForComplexFieldSearchQuery(final Set<ObjectId> locationIds) {
        return Criteria.where(LOCATION_PREFIX + MONGO_ID).in(locationIds);
    }

    private List<AbstractEvent> findEventsUsingSimpleFieldSearchQuery(final Query eventQueryForSimpleFieldSearch) {
        return new ArrayList<>(mongoTemplate.find(eventQueryForSimpleFieldSearch, AbstractEvent.class));
    }
}
