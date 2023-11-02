package com.spring3.firstproject.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring3.firstproject.integrationtests.testcontainers.AbstractIntegrationTest;
import com.spring3.firstproject.config.TestConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void shouldDisplaySwaggerUiPage() {
        String content = given()
            .basePath("/swagger-ui/index.html")
            .port(TestConfig.SERVER_PORT)
            .when()
                .get()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        assertTrue(content.contains("Swagger UI"));
	}

}
