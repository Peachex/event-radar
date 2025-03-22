package by.klevitov.synctaskscheduler.repository.impl;

import by.klevitov.synctaskscheduler.repository.TaskRepository;
import by.klevitov.synctaskscheduler.entity.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private static final String PERCENT_SYMBOL = "%";
    private final EntityManager entityManager;

    public TaskRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Task> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> taskRoot = criteriaQuery.from(Task.class);
        Predicate[] predicates = fields.entrySet().stream()
                .map(entry -> criteriaBuilder.like(
                        criteriaBuilder.lower(taskRoot.get(entry.getKey())),
                        PERCENT_SYMBOL + entry.getValue().toString().toLowerCase() + PERCENT_SYMBOL))
                .toArray(Predicate[]::new);
        criteriaQuery.where(createCombinedPredicates(predicates, isCombinedMatch, criteriaBuilder));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private Predicate createCombinedPredicates(final Predicate[] predicates, final boolean isCombinedMatch,
                                               final CriteriaBuilder criteriaBuilder) {
        return (isCombinedMatch ? criteriaBuilder.and(predicates) : criteriaBuilder.or(predicates));
    }
}
