package by.klevitov.synctaskscheduler.taskscheduler.repository;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskJpaRepository extends EntityJpaRepository<Task, Long>, TaskRepository {
    Optional<Task> findByNameIgnoreCase(String name);

    List<Task> findByStatus(TaskStatus status);
}
