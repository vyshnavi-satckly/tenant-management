package com.tenant_management.service;

import com.tenant_management.dto.TenantDatabaseRequest;
import com.tenant_management.dto.TenantDatabaseResponse;

import java.util.UUID;
import com.tenant_management.dto.DatabaseConnectionResult;
import com.tenant_management.dto.TenantDatabaseHealthResponse;

public interface TenantDatabaseService {
    DatabaseConnectionResult testConnection(UUID tenantId, TenantDatabaseRequest request);
    TenantDatabaseHealthResponse getHealth(UUID tenantId);


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