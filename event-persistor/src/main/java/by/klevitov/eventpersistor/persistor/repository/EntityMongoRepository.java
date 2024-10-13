package by.klevitov.eventpersistor.persistor.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityMongoRepository<E, ID> extends MongoRepository<E, ID> {
}
