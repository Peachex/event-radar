package by.klevitov.synctaskscheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityJpaRepository<E, ID> extends JpaRepository<E, ID> {
}
