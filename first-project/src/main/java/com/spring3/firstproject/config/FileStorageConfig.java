package com.spring3.firstproject.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "file") // obtem a configuração personalizada do yaml
// as propriedades representam os "filhos" e "filhos dos filhos" dessa propriedade personalizada "file"
public class FileStorageConfig {

    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
