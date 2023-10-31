package com.spring3.firstproject.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.spring3.firstproject.controllers.BookController;
import com.spring3.firstproject.data.vo.v1.BookVO;
import com.spring3.firstproject.exceptions.RequiredObjectIsNullException;
import com.spring3.firstproject.exceptions.ResourceNotFoundException;
import com.spring3.firstproject.mapper.ApplicationMapper;
import com.spring3.firstproject.model.Book;
import com.spring3.firstproject.repositories.BookRepository;

@Service
public class BookService {
    private Logger logger = Logger.getLogger(BookService.class.getName());

    @Autowired
    BookRepository repository;

    public List<BookVO> findAll() {
        logger.info("Finding all books!");

        List<BookVO> vos = ApplicationMapper.parseListObjects(repository.findAll(), BookVO.class);

        for (BookVO vo : vos) {
            vo.add(
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(BookController.class).findById(vo.getKey())
                ).withSelfRel()
            );
        }

        return vos;
    }

    public BookVO findById(Long id) {
        logger.info("Finding one book!");

        Book entity = repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No records found for this ID")
        );
        BookVO vo = ApplicationMapper.parseObject(entity, BookVO.class);

        vo.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BookController.class).findById(id)
            ).withSelfRel()
        );

        return vo;
    }

    public BookVO create(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        Book entity = ApplicationMapper.parseObject(book, Book.class);
        logger.info("Creating a book!");

        BookVO vo = ApplicationMapper.parseObject(repository.save(entity), BookVO.class);

        vo.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BookController.class).findById(vo.getKey())
            ).withSelfRel()
        );

        return vo;
    }

    public BookVO update(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a book!");

        Book entity = repository.findById(book.getKey()).orElseThrow(
            () -> new ResourceNotFoundException("No records found for this ID")
        );
        entity.setAuthor(book.getAuthor());
        entity.setTitle(book.getTitle());
        entity.setPrice(book.getPrice());
        entity.setLaunchDate(book.getLaunchDate());

        BookVO vo = ApplicationMapper.parseObject(repository.save(entity), BookVO.class);

        vo.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BookController.class).findById(vo.getKey())
            ).withSelfRel()
        );

        return vo;
    }

    public Void delete(Long id) {
        Book entity = repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No records found for this ID")
        );
        repository.delete(entity);

        return null;
    }
}
