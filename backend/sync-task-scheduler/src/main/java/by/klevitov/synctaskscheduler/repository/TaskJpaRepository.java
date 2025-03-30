package by.klevitov.synctaskscheduler.repository;

import by.klevitov.synctaskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.entity.TaskStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskJpaRepository extends EntityJpaRepository<Task, Long>, TaskRepository {
    Optional<Task> findByNameIgnoreCase(String name);

    List<Task> findByStatus(TaskStatus status);
}
