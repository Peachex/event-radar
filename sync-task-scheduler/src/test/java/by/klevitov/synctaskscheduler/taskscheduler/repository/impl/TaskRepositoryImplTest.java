package by.klevitov.synctaskscheduler.taskscheduler.repository.impl;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.repository.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskRepositoryImplTest {
    private EntityManager entityManager;
    private TaskRepository repository;

    @BeforeEach
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        repository = new TaskRepositoryImpl(entityManager);
    }

    @Test
    public void test_findByFields() {
        CriteriaBuilder mockedCriteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Task> mockedCriteriaQuery = Mockito.mock(CriteriaQuery.class);
        TypedQuery<Task> mockedTypedQuery = Mockito.mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder())
                .thenReturn(mockedCriteriaBuilder);
        when(mockedCriteriaBuilder.createQuery(Task.class))
                .thenReturn(mockedCriteriaQuery);
        when(entityManager.createQuery(mockedCriteriaQuery))
                .thenReturn(mockedTypedQuery);

        repository.findByFields(Map.of());
        verify(entityManager, times(1)).createQuery(mockedCriteriaQuery);
    }
}
