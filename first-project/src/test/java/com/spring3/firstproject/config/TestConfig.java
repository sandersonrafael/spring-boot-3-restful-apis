package com.spring3.firstproject.config;

public class TestConfig {

    public static final int SERVER_PORT = 8888;
    // public static final int SERVER_PORT = 80; // para testar o container subido no docker e não essa aplicação

    public static final String HEADER_PARAM_AUTHORIZATION = "Authorization";
    public static final String HEADER_PARAM_ORIGIN = "Origin";

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_YML = "application/x-yaml";

    public static final String ORIGIN_ERUDIO = "https://www.erudio.com.br";
    public static final String ORIGIN_SEMERU = "https://semeru.com.br";
}
