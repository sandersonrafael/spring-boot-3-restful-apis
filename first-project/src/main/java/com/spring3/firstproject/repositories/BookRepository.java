package com.spring3.firstproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring3.firstproject.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
