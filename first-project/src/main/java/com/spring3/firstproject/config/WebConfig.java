package com.spring3.firstproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Define que o Spring Boot precisa ler essa classe ao iniciar a execução
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // https://www.baeldung.com/spring-mvc-content-negotiation-json-xml
        // O content negotiation através de extension (url.xml | url.json | url.x-yaml, etc) foi depreciado

        // Utilizaremos parâmetro de url, como: http://url:port/path?mediaType=xml

        // favorParameter -> aceita parâmetros
        // parameterName -> nome do parâmetro
        // ignoreAcceptHeader -> ignora parâmetros no header da requisição
        // useRegisteredExtensionsOnly -> setar false para não usar somente o tipo determinado previamente
        // defaultContentType -> o tipo de resposta padrão da aplicação
        // mediaType -> define os tipos de media de resposta, da aplicação - pode ser definido múltiplas vezes
        configurer.favorParameter(true)
            .parameterName("mediaType")
            .ignoreAcceptHeader(true)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML);
    }

}
