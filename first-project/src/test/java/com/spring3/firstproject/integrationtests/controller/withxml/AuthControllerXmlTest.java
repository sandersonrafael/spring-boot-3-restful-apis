package com.spring3.firstproject.integrationtests.controller.withxml;

import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.spring3.firstproject.config.TestConfig;
import com.spring3.firstproject.integrationtests.testcontainers.AbstractIntegrationTest;
import com.spring3.firstproject.integrationtests.vo.AccountCredentialsVO;
import com.spring3.firstproject.integrationtests.vo.TokenVO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest {

    private static TokenVO tokenVO;

    @Test
    @Order(1)
    public void testSignin() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        tokenVO = given()
            .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .body(user)
            .when()
                .post()
            .then()
                .statusCode(200)
                .extract()
                .body()
                    .as(TokenVO.class);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(2)
    public void testRefresh() throws JsonMappingException, JsonProcessingException {
        // AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        TokenVO newTokenVO = given()
            .basePath("/auth/refresh")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .pathParam("username", tokenVO.getUsername())
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
            .when()
                .put("{username}")
            .then()
                .statusCode(200)
                .extract()
                .body()
                    .as(TokenVO.class);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}
