package ru.job4j.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.EmployeeRepository;
import ru.job4j.auth.repository.PersonRepository;
import ru.job4j.auth.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeRepository employees;
    private final PersonRepository persons;

    public EmployeeController(EmployeeRepository employees, PersonRepository persons) {
        this.employees = employees;
        this.persons = persons;
    }

    @GetMapping("/")
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        this.employees.findAll().spliterator().forEachRemaining(employees::add);
        return employees;
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> findById(@PathVariable int id) {
        var person = this.employees.findById(id);
        return new ResponseEntity<Employee>(
                person.orElse(new Employee()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Employee> create(HttpEntity<String> entity)
            throws JsonProcessingException {
        Employee employee = JsonUtil.buildEmployeeFromJson(entity.getBody());
        return new ResponseEntity<Employee>(
                this.employees.save(employee), HttpStatus.CREATED
        );
    }

    @PostMapping("{id}")
    public ResponseEntity<Employee> addNewAccount(@RequestBody Person person,
                                                  @PathVariable int id) {
        Employee employee = this.employees.findById(id).orElse(null);

        System.out.println("Found Employee=" + employee);
        if (employee == null) {
            return new ResponseEntity<>(new Employee(), HttpStatus.NOT_FOUND);
        }

        employee.addAccount(person);
        return new ResponseEntity<>(
                this.employees.save(employee),
                HttpStatus.ACCEPTED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        Employee persisted = employees.findById(employee.getId()).orElse(null);

        if (persisted == null) {
            return new ResponseEntity<Employee>(new Employee(), HttpStatus.NOT_FOUND);
        }

        persisted.setName(employee.getName());
        persisted.setSurname(employee.getName());
        persisted.setTaxInfoNumber(employee.getTaxInfoNumber());
        return new ResponseEntity<>(
                this.employees.save(persisted),
                HttpStatus.ACCEPTED
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        Employee toDelete = new Employee();
        toDelete.setId(id);
        this.employees.delete(toDelete);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/{emplId}/{accId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAccount(@PathVariable int emplId,
                                                @PathVariable int accId) {
        Employee employee = this.employees.findById(emplId).orElse(null);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        Person accountToDelete = employee.getAccounts()
                .stream()
                .filter(account -> account.getId() == accId)
                .findFirst()
                .get();

        employee.getAccounts().remove(accountToDelete);

        this.employees.save(employee);
        return ResponseEntity.ok().build();
    }
}