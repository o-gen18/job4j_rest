package ru.job4j.auth.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String taxInfoNumber;
    private Timestamp hired;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Person> accounts = new HashSet<>();

    public static Employee of(String name, String surname, String taxInfoNumber) {
        Employee employee = new Employee();
        employee.name = name;
        employee.surname = surname;
        employee.taxInfoNumber = taxInfoNumber;
        employee.hired = new Timestamp(System.currentTimeMillis());
        return employee;
    }

    public void addAccount(Person person) {
        this.accounts.add(person);
    }

    public boolean removeAccount(Person person) {
        return this.accounts.remove(person);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTaxInfoNumber() {
        return taxInfoNumber;
    }

    public void setTaxInfoNumber(String taxInfoNumber) {
        this.taxInfoNumber = taxInfoNumber;
    }

    public Timestamp getHired() {
        return hired;
    }

    public void setHired(Timestamp hired) {
        this.hired = hired;
    }

    public Set<Person> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Person> accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Employee{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", surname='" + surname + '\''
                + ", taxInfoNumber='" + taxInfoNumber + '\''
                + ", hired=" + hired
                + ", accounts=" + accounts
                + '}';
    }
}
