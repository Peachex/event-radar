package by.klevitov.eventpersistor.repository.impl;

import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.repository.LocationRepository;
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
    private static final String COUNTRY_FIELD_NAME = "country";
    private static final String CITY_FIELD_NAME = "city";
    private final MongoTemplate mongoTemplate;

    public LocationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Location> findByCountryAndCityIgnoreCase(final List<Location> locations) {
        List<String> countries = locations.stream().map(Location::getCountry).toList();
        List<String> cities = locations.stream().map(Location::getCity).toList();
        final Query query = new Query();
        query.addCriteria(
                where(COUNTRY_FIELD_NAME)
                        .in(countries)
                        .and(CITY_FIELD_NAME)
                        .in(cities));
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
}
