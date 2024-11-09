package by.klevitov.synctaskscheduler.taskscheduler.repository;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskJpaRepository extends EntityJpaRepository<Task, Long>, TaskRepository {
    Optional<Task> findByNameIgnoreCase(String name);
}
