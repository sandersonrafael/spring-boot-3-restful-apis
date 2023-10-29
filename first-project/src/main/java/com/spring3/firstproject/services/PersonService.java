package com.spring3.firstproject.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

// importados para poder utilizar o hateoas
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import org.springframework.stereotype.Service;

import com.spring3.firstproject.controllers.PersonController;
import com.spring3.firstproject.data.vo.v1.PersonVO;
import com.spring3.firstproject.exceptions.RequiredObjectIsNullException;
// import com.spring3.firstproject.data.vo.v2.PersonVOV2;
import com.spring3.firstproject.exceptions.ResourceNotFoundException;
import com.spring3.firstproject.mapper.ApplicationMapper;
// import com.spring3.firstproject.mapper.custom.PersonMapper;
import com.spring3.firstproject.model.Person;
import com.spring3.firstproject.repositories.PersonRepository;

@Service
public class PersonService {
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository repository;

    // @Autowired
    // PersonMapper mapperV2;

    public List<PersonVO> findAll() {
        logger.info("Finding all persons!");

        // adicionando o link hateoas
        List<PersonVO> vos = ApplicationMapper.parseListObjects(repository.findAll(), PersonVO.class);
        for (PersonVO vo : vos) {
            vo.add(
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PersonController.class).findById(vo.getKey())
                ).withSelfRel()
            );
        }

        return vos;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person!");
        PersonVO person = new PersonVO();

        person.setFirstName("Fulano");
        person.setLastName("Silva");
        person.setAddress("Brasília - DF - Brasil");
        person.setGender("Male");

        Person entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        PersonVO vo = ApplicationMapper.parseObject(entity, PersonVO.class);

        // adicionando o link hateoas
        vo.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PersonController.class).findById(id)
            ).withSelfRel()
        );
        return vo;
    }

    public PersonVO create(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        Person entity = ApplicationMapper.parseObject(person, Person.class);
        logger.info("Creating a person!");

        PersonVO vo = ApplicationMapper.parseObject(repository.save(entity), PersonVO.class);

        // adicionando o link hateoas
        vo.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PersonController.class).findById(vo.getKey())
            ).withSelfRel()
        );
        return vo;
    }

    // trata-se da V2 da API
    // public PersonVOV2 createV2(PersonVOV2 person) {
    //     Person entity = mapperV2.convertVoToEntity(person);
    //     logger.info("Creating a person!");

    //     // Nesse caso, o Dozer não consegue converter, porque o PersonVOV2 tem um campo a mais que o Person -
    //     // retornado do repository.save((Person) entity)
    //     // para resolver esse problema, será criado um Mapper Customizado
    //     PersonVOV2 vo = mapperV2.convertEntityToVo(repository.save(entity));
    //     return vo;
    // }

    public PersonVO update(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a person!");

        Person entity = repository.findById(person.getKey())
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        PersonVO vo = ApplicationMapper.parseObject(repository.save(entity), PersonVO.class);

        // adicionando o link hateoas

        vo.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PersonController.class).findById(vo.getKey())
            ).withSelfRel()
        );
        return vo;
    }

    public Void delete(Long id) {
        Person entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);

        return null;
    }
}
