package com.spring3.firstproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring3.firstproject.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
