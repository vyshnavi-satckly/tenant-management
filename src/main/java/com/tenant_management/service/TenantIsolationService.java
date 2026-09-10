package com.tenant_management.service;


import java.util.UUID;

import com.tenant_management.dto.request.TenantIsolationRequestDto;
import com.tenant_management.dto.response.TenantIsolationResponseDto;
import com.tenant_management.dto.response.TenantIsolationStatusResponseDto;

public interface TenantIsolationService {

    TenantIsolationResponseDto getIsolation(UUID tenantId);

    TenantIsolationResponseDto updateIsolation(
            UUID tenantId,
            TenantIsolationRequestDto request
    );

    TenantIsolationStatusResponseDto getIsolationStatus(
            UUID tenantId
    );
}
