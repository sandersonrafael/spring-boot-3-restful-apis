package com.spring3.firstproject.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.spring3.firstproject.config.FileStorageConfig;
import com.spring3.firstproject.exceptions.FileStorageException;
import com.spring3.firstproject.exceptions.MyFileNotFoundException;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir())
            .toAbsolutePath().normalize();

        this.fileStorageLocation = path;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where oploaded files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // neste espaço, fazemos as verificações necessárias conforme regras de negócio
            if (fileName.contains("..")) {
                throw new FileStorageException("Error! File name contains an invalid path sequence: " + fileName);
            }

            // para salvar em nuvem etc, basta alterar essas duas linhas
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }


    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) return resource;

            throw new  MyFileNotFoundException("File not found: " + fileName);
        } catch (Exception e) {
            throw new MyFileNotFoundException("File not found: " + fileName, e);
        }
    }
}
