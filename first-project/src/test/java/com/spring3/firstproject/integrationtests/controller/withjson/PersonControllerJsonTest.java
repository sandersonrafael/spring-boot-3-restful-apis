package com.spring3.firstproject.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring3.firstproject.integrationtests.testcontainers.AbstractIntegrationTest;
import com.spring3.firstproject.integrationtests.vo.AccountCredentialsVO;
import com.spring3.firstproject.integrationtests.vo.PersonVO;
import com.spring3.firstproject.integrationtests.vo.TokenVO;
import com.spring3.firstproject.integrationtests.vo.wrappers.WrapperPersonVO;
import com.spring3.firstproject.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        // utilizado para que os links HATEOAS não resultem em erro por trazer um objeto com campos além do que há definido pela classe (_links)
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        String accessToken = given()
            .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(user)
            .when()
                .post()
            .then()
                .statusCode(200)
                .extract()
                .body()
                    .as(TokenVO.class)
                        .getAccessToken();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
            .setBasePath("/api/person/v1")
            .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
    }

	@Test
    @Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        String content = given().spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
            .when()
                .post()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Nelson", persistedPerson.getFirstName());
        assertEquals("Piquet", persistedPerson.getLastName());
        assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
	}

    @Test
    @Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
        person.setLastName("Piquet Souto Maior");

        String content = given().spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
            .when()
                .post()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());

        assertEquals(person.getId(), persistedPerson.getId());

        assertEquals("Nelson", persistedPerson.getFirstName());
        assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
        assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
	}

    @Test
    @Order(3)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        String content = given().spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .pathParam("id", person.getId())
            .when()
                .patch("/{id}")
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertFalse(persistedPerson.getEnabled());

        assertEquals(person.getId(), persistedPerson.getId());

        assertEquals("Nelson", persistedPerson.getFirstName());
        assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
        assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
	}

    @Test
    @Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        String content = given().spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .pathParam("id", person.getId())
            .when()
                .get("/{id}")
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertFalse(persistedPerson.getEnabled());

        assertEquals(person.getId(), persistedPerson.getId());

        assertEquals("Nelson", persistedPerson.getFirstName());
        assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
        assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
	}

    @Test
    @Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
        given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .pathParam("id", person.getId())
            .when()
                .delete("/{id}")
            .then()
                .statusCode(204);
	}

    @Test
    @Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        String content = given().spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .queryParams(
                "page", 3,
                "size", 10,
                "direction", "asc"
            )
            .when()
                .get()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();
                    // .as(new TypeRef<List<PersonVO>>() {});
        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        List<PersonVO> contentList = wrapper.getEmbedded().getPersons();

        PersonVO personFoundOne = contentList.get(0);

        assertNotNull(personFoundOne.getId());
        assertNotNull(personFoundOne.getFirstName());
        assertNotNull(personFoundOne.getLastName());
        assertNotNull(personFoundOne.getAddress());
        assertNotNull(personFoundOne.getGender());
        assertFalse(personFoundOne.getEnabled());

        assertEquals(949, personFoundOne.getId());

        assertEquals("Alwyn", personFoundOne.getFirstName());
        assertEquals("Piscot", personFoundOne.getLastName());
        assertEquals("0529 Sugar Terrace", personFoundOne.getAddress());
        assertEquals("Male", personFoundOne.getGender());

        PersonVO personFoundTwo = contentList.get(1);

        assertNotNull(personFoundTwo.getId());
        assertNotNull(personFoundTwo.getFirstName());
        assertNotNull(personFoundTwo.getLastName());
        assertNotNull(personFoundTwo.getAddress());
        assertNotNull(personFoundTwo.getGender());
        assertFalse(personFoundTwo.getEnabled());

        assertEquals(511, personFoundTwo.getId());

        assertEquals("Amalita", personFoundTwo.getFirstName());
        assertEquals("Leah", personFoundTwo.getLastName());
        assertEquals("0 Kropf Way", personFoundTwo.getAddress());
        assertEquals("Female", personFoundTwo.getGender());
	}

    @Test
    @Order(7)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        String content = given().spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParam("firstName", "feo")
            .queryParams(
                "page", 0,
                "size", 6,
                "direction", "asc"
            )
            .when()
                .get("/findByName/{firstName}")
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();
                    // .as(new TypeRef<List<PersonVO>>() {});
        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        List<PersonVO> contentList = wrapper.getEmbedded().getPersons();

        PersonVO personFoundTwo = contentList.get(0);

        assertNotNull(personFoundTwo.getId());
        assertNotNull(personFoundTwo.getFirstName());
        assertNotNull(personFoundTwo.getLastName());
        assertNotNull(personFoundTwo.getAddress());
        assertNotNull(personFoundTwo.getGender());
        assertFalse(personFoundTwo.getEnabled());

        assertEquals(981, personFoundTwo.getId());

        assertEquals("Feodora", personFoundTwo.getFirstName());
        assertEquals("Archdeckne", personFoundTwo.getLastName());
        assertEquals("41 Florence Street", personFoundTwo.getAddress());
        assertEquals("Female", personFoundTwo.getGender());
	}

    @Test
    @Order(8)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
            .setBasePath("/api/person/v1")
            .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        given().spec(specificationWithoutToken)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .when()
                .get()
            .then()
                .statusCode(403);
	}

    private void mockPerson() {
        person.setFirstName("Nelson");
        person.setLastName("Piquet");
        person.setAddress("Brasília - DF - Brasil");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
