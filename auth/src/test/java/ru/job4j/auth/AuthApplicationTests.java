package ru.job4j.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.controller.PersonController;
import ru.job4j.auth.domain.Person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonController controller;

    @Test
    public void whenCreateThenCreated() throws Exception {
        Person person = new Person();
        person.setLogin("Person");
        person.setPassword("Password");
        controller.create(person);

        String jsonParam = "{\"login\":\"job4j@gmail.com\",\"password\":\"123\"}";
        mockMvc.perform(post("/person/")
                .contentType("application/json")
                .content(jsonParam))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("login").value("job4j@gmail.com"))
                .andExpect(jsonPath("password").value("123"));
    }

    @Test
    public void whenGetThenGetAll() throws Exception {
        Person person1 = new Person();
        person1.setLogin("Person First");
        person1.setPassword("PasswordFirst111");

        Person person2 = new Person();
        person2.setLogin("Person Second");
        person2.setPassword("PasswordSecond222");
        controller.create(person1);
        controller.create(person2);

        mockMvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].login").value("Person First"))
                .andExpect(jsonPath("$[0].password").value("PasswordFirst111"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].login").value("Person Second"))
                .andExpect(jsonPath("$[1].password").value("PasswordSecond222"));
    }

    @Test
    public void whenGetByIdThenReturnExactValue() throws Exception {
        Person person1 = new Person();
        person1.setLogin("Person First");
        person1.setPassword("PasswordFirst111");

        Person person2 = new Person();
        person2.setLogin("Person Second");
        person2.setPassword("PasswordSecond222");
        controller.create(person1);
        controller.create(person2);

        mockMvc.perform(get("/person/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("login").value("Person Second"))
                .andExpect(jsonPath("password").value("PasswordSecond222"));
    }

    @Test
    public void whenUpdateThenReturnUpdated() throws Exception {
        Person person = new Person();
        person.setLogin("Person");
        person.setPassword("Password");
        controller.create(person);

        String jsonParam = "{\"id\":\"1\",\"login\":\"job4j@gmail.com\",\"password\":\"123\"}";
        mockMvc.perform(put("/person/")
                .contentType("application/json")
                .content(jsonParam))
                .andExpect(status().isOk())
                .andDo((resultHandler) -> this.mockMvc.perform(get("/person/1"))
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("login").value("job4j@gmail.com"))
                        .andExpect(jsonPath("password").value("123")));
    }

    @Test
    public void whenDeleteThenReturnNull() throws Exception {
        Person person = new Person();
        person.setLogin("Person");
        person.setPassword("Password");
        controller.create(person);

        mockMvc.perform(delete("/person/1"))
                .andExpect(status().isOk())
                .andDo((resultHandler) -> this.mockMvc.perform(get("/person/1"))
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("login").doesNotExist())
                        .andExpect(jsonPath("password").doesNotExist()));
    }
}