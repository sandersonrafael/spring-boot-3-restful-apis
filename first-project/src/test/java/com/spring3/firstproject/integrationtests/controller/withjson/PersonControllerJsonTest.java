package com.spring3.firstproject.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

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
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
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

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Richard", persistedPerson.getFirstName());
        assertEquals("Stallman", persistedPerson.getLastName());
        assertEquals("New York City, New york, US", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
	}

	@Test
    @Order(2)
	public void testCreateWithUnauthorizedOrigin() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        String content = given().spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_SEMERU)
                .body(person)
            .when()
                .post()
            .then()
                .statusCode(403)
            .extract()
                .body()
                    .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
	}

    @Test
    @Order(3)
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

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Richard", persistedPerson.getFirstName());
        assertEquals("Stallman", persistedPerson.getLastName());
        assertEquals("New York City, New york, US", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
	}

    @Test
    @Order(4)
	public void testFindByIdWithUnauthorizedOrigin() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        String content = given().spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_SEMERU)
                .pathParam("id", person.getId())
            .when()
                .get("/{id}")
            .then()
                .statusCode(403)
            .extract()
                .body()
                    .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
	}

    private void mockPerson() {
        person.setFirstName("Richard");
        person.setLastName("Stallman");
        person.setAddress("New York City, New york, US");
        person.setGender("Male");
    }
}
