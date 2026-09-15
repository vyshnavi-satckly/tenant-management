package com.tenant_management.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

        String store( String tenantId,
                      String fileType,
                      MultipartFile file );
    }

