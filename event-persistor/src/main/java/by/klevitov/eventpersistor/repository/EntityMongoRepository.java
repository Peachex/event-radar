package by.klevitov.eventpersistor.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityMongoRepository<E, ID> extends MongoRepository<E, ID> {
}
