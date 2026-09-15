package com.tenant_management.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {


    private String uploadDirectory="uploads";

    private String baseUrl="http://localhost:8080";

    @Override
    public String store(
            String tenantId,
            String fileType,
            MultipartFile file) {

        try {
            String originalFileName = file.getOriginalFilename();

            String extension = originalFileName.substring(
                    originalFileName.lastIndexOf(".")
            );

            // Random name prevents two users from overwriting the same file.
            String fileName = fileType + "_" +UUID.randomUUID() + extension;

            Path folderPath = Paths.get(
                    uploadDirectory,
                    "tenant-branding"
            );

            // Creates uploads/tenant-branding if absent.
            Files.createDirectories(folderPath);

            Path filePath = folderPath.resolve(fileName);

            // Copies MultipartFile image bytes into the physical folder.
            Files.copy(
                    file.getInputStream(),
                    filePath
            );

            return baseUrl
                    + "/uploads/tenant-branding/"
                    + fileName;

        } catch (IOException exception) {
            throw new RuntimeException("Unable to store file", exception);
        }
    }

}
