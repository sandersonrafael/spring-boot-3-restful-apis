package com.spring3.firstproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring3.firstproject.data.vo.v1.PersonVO;
// import com.spring3.firstproject.data.vo.v2.PersonVOV2;
import com.spring3.firstproject.services.PersonService;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

    @Autowired
    private PersonService service;

    /*
        produces = MediaType.APPLICATION_JSON_VALUE e consumes não são necessários hoje em dia
        mas é recomendado usar para quando for utilizar o swagger para documentar a API

        produces = MediaType.APPLICATION_XML_VALUE -> determina que a aplicação serve XML
    */
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    public List<PersonVO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    public PersonVO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PostMapping(
        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    ) public PersonVO create(@RequestBody PersonVO person) {
        return service.create(person);
    }

    // trata-se de uma V2 do método create, do verbo POST (Atualização de API)
    // @PostMapping(
    //     value = "/v2",
    //     consumes = MediaType.APPLICATION_JSON_VALUE,
    //     produces = MediaType.APPLICATION_JSON_VALUE
    // ) public PersonVOV2 createV2(@RequestBody PersonVOV2 person) {
    //     return service.createV2(person);
    // }

    @PutMapping(
        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    ) public PersonVO update(@RequestBody PersonVO person) {
        return service.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
