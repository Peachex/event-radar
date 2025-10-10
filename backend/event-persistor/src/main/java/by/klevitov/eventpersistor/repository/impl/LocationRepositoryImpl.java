package by.klevitov.eventpersistor.repository.impl;

import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class LocationRepositoryImpl implements LocationRepository {
    private static final String RAW_ADDRESS_FIELD_NAME = "rawAddress";
    private static final String LOCATION_NAME_FIELD_NAME = "name";
    private final MongoTemplate mongoTemplate;

    public LocationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Location> findByRawAddressAndNameIgnoreCase(final List<Location> locations) {
        List<String> rawAddresses = locations.stream().map(Location::getRawAddress).toList();
        List<String> names = locations.stream().map(Location::getName).toList();
        final Query query = new Query();
        query.addCriteria(
                where(RAW_ADDRESS_FIELD_NAME)
                        .in(rawAddresses)
                        .and(LOCATION_NAME_FIELD_NAME)
                        .in(names));
        return mongoTemplate.find(query, Location.class);
    }

    @Override
    public List<Location> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch) {
        final Query query = new Query();
        final List<Criteria> criteriaList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            criteriaList.add(Criteria.where(entry.getKey()).regex(compile(entry.getValue().toString(), CASE_INSENSITIVE)));
        }
        query.addCriteria(isCombinedMatch
                ? new Criteria().andOperator(criteriaList)
                : new Criteria().orOperator(criteriaList));
        return mongoTemplate.find(query, Location.class);
    }

    @Override
    public Page<Location> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch,
                                       final PageRequest pageRequest) {
        final Query query = new Query();
        final List<Criteria> criteriaList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            criteriaList.add(Criteria.where(entry.getKey()).regex(compile(entry.getValue().toString(), CASE_INSENSITIVE)));
        }
        query.addCriteria(isCombinedMatch
                ? new Criteria().andOperator(criteriaList)
                : new Criteria().orOperator(criteriaList));
        query.with(pageRequest);
        List<Location> locations = mongoTemplate.find(query, Location.class);
        long totalCount = mongoTemplate.count(query, Location.class);
        return new PageImpl<>(locations, pageRequest, totalCount);
    }
}
