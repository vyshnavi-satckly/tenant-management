package com.tenant_management.service;

import com.tenant_management.dto.request.TenantBrandingRequestDto;
import com.tenant_management.dto.response.TenantBrandingFileUploadResponseDto;
import com.tenant_management.dto.response.TenantBrandingPreviewResponseDto;
import com.tenant_management.dto.response.TenantBrandingResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface TenantBrandingService {

    TenantBrandingResponseDto getBranding(String tenantId);

    TenantBrandingResponseDto updateBranding( String tenantId,
                                              TenantBrandingRequestDto request );

    TenantBrandingFileUploadResponseDto uploadLogo( String tenantId,
                                                    MultipartFile logo );

    TenantBrandingFileUploadResponseDto uploadBackground( String tenantId,
                                                          MultipartFile background );

    TenantBrandingPreviewResponseDto getPreview(String tenantId);
}