package by.klevitov.eventpersistor.repository;

import by.klevitov.eventpersistor.entity.Location;

import java.util.Optional;

public interface LocationMongoRepository extends EntityMongoRepository<Location, String>, LocationRepository {
    Optional<Location> findByCountryAndCityIgnoreCase(String country, String city);
}
