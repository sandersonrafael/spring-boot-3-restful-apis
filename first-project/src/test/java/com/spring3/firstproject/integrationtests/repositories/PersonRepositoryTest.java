package com.spring3.firstproject.integrationtests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.spring3.firstproject.integrationtests.testcontainers.AbstractIntegrationTest;
import com.spring3.firstproject.model.Person;
import com.spring3.firstproject.repositories.PersonRepository;

// @ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository repository;

    private static Person person;

    @BeforeAll
    public static void setup() {
        person = new Person();
    }

    @Test
    @Order(1)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));

        person = repository.findPersonsByName("feo", pageable).getContent().get(0);
        System.out.println(person);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertFalse(person.getEnabled());

        assertEquals(981, person.getId());

        assertEquals("Feodora", person.getFirstName());
        assertEquals("Archdeckne", person.getLastName());
        assertEquals("41 Florence Street", person.getAddress());
        assertEquals("Female", person.getGender());
	}

    @Test
    @Order(2)
	public void testDisablePerson() throws JsonMappingException, JsonProcessingException {
        repository.disablePerson(person.getId());

        Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));

        person = repository.findPersonsByName("feo", pageable).getContent().get(0);
        System.out.println(person);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertFalse(person.getEnabled());

        assertEquals(981, person.getId());

        assertEquals("Feodora", person.getFirstName());
        assertEquals("Archdeckne", person.getLastName());
        assertEquals("41 Florence Street", person.getAddress());
        assertEquals("Female", person.getGender());
	}
}
