package ru.job4j.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;

public class JsonUtil {
    /**
     * Builds an Employee object having json array as an input.
     * The Json contains an array where the first element describes the employee object's fields
     * and the second element is itself an array of json Person objects that are to become
     * employee's accounts.
     * Expected json array format:
     * [{Employee object values},[{Person object values},...]]
     * @param jsonArray The json array to be parsed.
     * @return The compiled Employee object with its accounts.
     * @throws JsonProcessingException Might be thrown.
     */
    public static Employee buildEmployeeFromJson(final String jsonArray)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        final JsonNode jsonNode = mapper.readTree(jsonArray);

        Iterator<JsonNode> jsonNodeIter = jsonNode.iterator();

        Employee employee = mapper.readValue(jsonNodeIter.next().toString(), Employee.class);

        Person[] accounts = mapper.readValue(jsonNodeIter.next().toString(), Person[].class);

        employee.setAccounts(Set.of(accounts));

        employee.setHired(new Timestamp(System.currentTimeMillis()));

        return employee;
    }
}
