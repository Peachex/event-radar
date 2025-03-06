package by.klevitov.eventpersistor.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface EntityMongoRepository<E, ID> extends MongoRepository<E, ID>, PagingAndSortingRepository<E, ID> {
}
