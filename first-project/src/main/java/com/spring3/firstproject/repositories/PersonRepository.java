package com.spring3.firstproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring3.firstproject.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
