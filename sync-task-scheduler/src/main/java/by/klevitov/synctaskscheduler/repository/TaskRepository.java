package by.klevitov.synctaskscheduler.repository;

import by.klevitov.synctaskscheduler.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaskRepository {
    List<Task> findByFields(Map<String, Object> fields, boolean isCombinedMatch);
}
