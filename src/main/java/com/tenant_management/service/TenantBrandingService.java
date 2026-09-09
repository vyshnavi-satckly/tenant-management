package com.tenant_management.service;

import com.tenant_management.dto.request.TenantBrandingRequestDto;
import com.tenant_management.dto.response.TenantBrandingFileUploadResponseDto;
import com.tenant_management.dto.response.TenantBrandingPreviewResponseDto;
import com.tenant_management.dto.response.TenantBrandingResponseDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TenantBrandingService {

    public TenantBrandingResponseDto getBranding(String tenantId) {
    }

    public TenantBrandingResponseDto updateBranding(String tenantId, @Valid TenantBrandingRequestDto request) {
    }

    public TenantBrandingFileUploadResponseDto uploadLogo(String tenantId, MultipartFile logo) {
    }

    public TenantBrandingFileUploadResponseDto uploadBackground(String tenantId, MultipartFile background) {
    }

    public TenantBrandingPreviewResponseDto getPreview(String tenantId) {
    }
}
