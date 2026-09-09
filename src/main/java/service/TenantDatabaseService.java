package com.tenant_management.service;

import com.tenant_management.dto.TenantDatabaseRequest;
import com.tenant_management.dto.TenantDatabaseResponse;

import java.util.UUID;

public interface TenantDatabaseService {

    TenantDatabaseResponse getDatabase(UUID tenantId);

    TenantDatabaseResponse updateDatabase(
            UUID tenantId,
            TenantDatabaseRequest request
    );

    TenantDatabaseResponse createDatabase(
            UUID tenantId,
            TenantDatabaseRequest request
    );
}