package com.spring3.firstproject.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.spring3.firstproject.serialization_converter.YamlJackson2HttpMessageConverter;

@Configuration // Define que o Spring Boot precisa ler essa classe ao iniciar a execução
public class WebConfig implements WebMvcConfigurer {

    // Necessário adicionar o MediaType para usar o yaml, depois utilizar na declaração de tipos permitidos
    private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");

    // configuração do CORS
    @Value("${cors.origin-patterns}")
    private String corsOriginPatterns = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = corsOriginPatterns.split(",");
        registry.addMapping("/**") // rotas onde o cors específico se aplica
            // .allowedMethods("GET", "POST", "PUT", "DELETE") // métodos permitidos
            .allowedMethods("*") // para permitir todos, usamos *
            .allowedOrigins(allowedOrigins) // origins aceitas conforme array de String obtido
            .allowCredentials(true); // permitir autenticação
    }

    // necessário implementar esse método chamando o converter criado para que seja gerado o yml
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlJackson2HttpMessageConverter());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // https://www.baeldung.com/spring-mvc-content-negotiation-json-xml
        // O content negotiation através de extension (url.xml | url.json | url.x-yaml, etc) foi depreciado


        /* Via Query Param

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
        */

        /* Via Header Param

        Configuração com os mesmos métodos, mas favorParameter e ignoreAcceptHeader com valores false
        Sem o método .parameterName, pois não receberá parâmetro

        No header da requisição, utilizar:
        Accept: application/xml
        */
        configurer.favorParameter(false)
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML);
    }
}
