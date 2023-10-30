package com.spring3.firstproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean // objeto instanciado, montado e gerenciado pelo spring ioc container
    OpenAPI customOpenAPI() {
        return new OpenAPI().info(
            new Info().title(
                "Spring Boot 3 RESTful API"
            ).version(
                "v1"
            ).description(
                "Descrição sobre a API"
            ).termsOfService(
                "https://google.com"
            ).license(
                new License().name("Apache 2.0").url("https://google.com")
            )
        );
    }
}
