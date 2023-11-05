package com.spring3.firstproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring3.firstproject.data.vo.v1.PersonVO;
// import com.spring3.firstproject.data.vo.v2.PersonVOV2;
import com.spring3.firstproject.services.PersonService;
import com.spring3.firstproject.util.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

// @CrossOrigin // CORS
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People") // Annotation para config do Swagger
public class PersonController {

    @Autowired
    private PersonService service;

    /*
        produces = MediaType.APPLICATION_JSON_VALUE e consumes não são necessários hoje em dia
        mas é recomendado usar para quando for utilizar o swagger para documentar a API

        produces = MediaType.APPLICATION_XML_VALUE -> determina que a aplicação serve XML
    */
    @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML } )
    // doc swagger config
    @Operation(
        summary = "Finds all people", description = "Finds all people", tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                )
            }),
            // somente @Content cria um default -> usar quando não for um retorno de VO ou DTO
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
    public ResponseEntity<Page<PersonVO>> findAll( // separando resposta por páginas, para melhorar desempenho
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "12") Integer size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        // ordenação ascendente ou descendente, ignorando maiúsculas ou minúsculas
        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        // interface e classe responsáveis por paginar no spring framework
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    // @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML } )
    // doc swagger config
    @Operation(
        summary = "Finds a person", description = "Finds a person", tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                schema = @Schema(implementation = PersonVO.class)
            )),
            // somente @Content cria um default -> usar quando não for um retorno de VO ou DTO
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
    public PersonVO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    // @CrossOrigin(origins = {"http://localhost:8080", "https://erudio.com.br"})
    @PostMapping(
        consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
        produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML }
    )
    // doc swagger config
    @Operation(
        summary = "Adds a new person", description = "Adds a new person", tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                schema = @Schema(implementation = PersonVO.class)
            )),
            // somente @Content cria um default -> usar quando não for um retorno de VO ou DTO
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
    public PersonVO create(@RequestBody PersonVO person) {
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
        consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
        produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML }
    )
    // doc swagger config
    @Operation(
        summary = "Updates a person", description = "Updates a person", tags = {"People"},
        responses = {
            @ApiResponse(description = "Updated", responseCode = "200", content = @Content(
                schema = @Schema(implementation = PersonVO.class)
            )),
            // somente @Content cria um default -> usar quando não for um retorno de VO ou DTO
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
    public PersonVO update(@RequestBody PersonVO person) {
        return service.update(person);
    }

    @PatchMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML } )
    // doc swagger config
    @Operation(
        summary = "Disables a person", description = "Disables a person", tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                schema = @Schema(implementation = PersonVO.class)
            )),
            // somente @Content cria um default -> usar quando não for um retorno de VO ou DTO
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
    public PersonVO disablePerson(@PathVariable(value = "id") Long id) {
        return service.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}")
    // doc swagger config
    @Operation(
        summary = "Deletes a person", description = "Deletes a person", tags = {"People"},
        responses = {
            // Como o delete não possui retorno de objeto, podemos deixar só o @Content também
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            // somente @Content cria um default -> usar quando não for um retorno de VO ou DTO
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
