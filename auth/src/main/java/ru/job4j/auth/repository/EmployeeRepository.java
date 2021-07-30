package ru.job4j.auth.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.auth.domain.Employee;

import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    String QUERY = "SELECT DISTINCT e FROM Employee e FULL JOIN FETCH e.accounts ";

    @Override
    @Query(QUERY)
    Iterable<Employee> findAll();

    @Override
    @Query(QUERY + "WHERE e.id =:ID")
    Optional<Employee> findById(@Param("ID") Integer id);
}
