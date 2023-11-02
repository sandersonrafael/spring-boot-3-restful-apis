package com.spring3.firstproject.integrationtests.controller.withyaml;

import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.spring3.firstproject.config.TestConfig;
import com.spring3.firstproject.integrationtests.controller.withyaml.mapper.YMLMapper;
import com.spring3.firstproject.integrationtests.testcontainers.AbstractIntegrationTest;
import com.spring3.firstproject.integrationtests.vo.AccountCredentialsVO;
import com.spring3.firstproject.integrationtests.vo.TokenVO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static YMLMapper objectMapper;
    private static TokenVO tokenVO;

    @BeforeAll
    public static void setup() {
        objectMapper = new YMLMapper();
    }

    @Test
    @Order(1)
    public void testSignin() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        RequestSpecification specification = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // logar as informações do request
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // logar as informações do response
            .build();

        tokenVO = given()
            .spec(specification)
            .config(
                RestAssuredConfig.config().encoderConfig(
                    EncoderConfig.encoderConfig().encodeContentTypeAs(
                        TestConfig.CONTENT_TYPE_YML, ContentType.TEXT
                    )
                )
            )
            .accept(TestConfig.CONTENT_TYPE_YML)
            .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .body(user, objectMapper)
            .when()
                .post()
            .then()
                .statusCode(200)
                .extract()
                .body()
                    .as(TokenVO.class, objectMapper);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(2)
    public void testRefresh() throws JsonMappingException, JsonProcessingException {
        // AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        TokenVO newTokenVO = given()
            .config(
                RestAssuredConfig.config().encoderConfig(
                    EncoderConfig.encoderConfig().encodeContentTypeAs(
                        TestConfig.CONTENT_TYPE_YML, ContentType.TEXT
                    )
                )
            )
            .accept(TestConfig.CONTENT_TYPE_YML)
            .basePath("/auth/refresh")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .pathParam("username", tokenVO.getUsername())
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
            .when()
                .put("{username}")
            .then()
                .statusCode(200)
                .extract()
                .body()
                    .as(TokenVO.class, objectMapper);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}
