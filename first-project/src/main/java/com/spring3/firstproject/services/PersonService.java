package com.spring3.firstproject.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring3.firstproject.exceptions.ResourceNotFoundException;
import com.spring3.firstproject.model.Person;
import com.spring3.firstproject.repositories.PersonRepository;

@Service
public class PersonService {
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository repository;

    public List<Person> findAll() {
        logger.info("Finding all persons!");

        return repository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Finding one person!");
        Person person = new Person();

        person.setFirstName("Fulano");
        person.setLastName("Silva");
        person.setAddress("Brasília - DF - Brasil");
        person.setGender("Male");

        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    }

    public Person create(Person person) {
        logger.info("Creating a person!");
        return repository.save(person);
    }

    public Person update(Person person) {
        logger.info("Updating a person!");

        Person entity = repository.findById(person.getId())
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(person);
    }

    public Void delete(Long id) {
        Person entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);

        return null;
    }
}
