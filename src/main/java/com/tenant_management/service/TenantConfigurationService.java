package com.tenant_management.service;

import com.tenant_management.dto.request.TenantConfigurationRequestDto;
import com.tenant_management.dto.response.TenantConfigurationResponseDto;

import java.util.UUID;

public interface TenantConfigurationService {

    TenantConfigurationResponseDto getConfiguration(UUID tenantId);

    TenantConfigurationResponseDto updateConfiguration(
            UUID tenantId,
            TenantConfigurationRequestDto request,
            UUID updatedBy
    );
}