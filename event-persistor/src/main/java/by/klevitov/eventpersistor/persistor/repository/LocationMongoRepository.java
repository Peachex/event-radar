package by.klevitov.eventpersistor.persistor.repository;

import by.klevitov.eventpersistor.persistor.entity.Location;

import java.util.Optional;

public interface LocationMongoRepository extends EntityMongoRepository<Location, String> {
    Optional<Location> findByCountryAndCityIgnoreCase(String country, String city);
}
