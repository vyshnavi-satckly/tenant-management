package com.tenant_management.service;

import java.util.Map;
import java.util.UUID;

import com.tenant_management.dto.TenantDatabaseRequest;
import com.tenant_management.dto.TenantDatabaseResponse;

public interface TenantDatabaseService {

    TenantDatabaseResponse getDatabase(UUID tenantId);

    TenantDatabaseResponse updateDatabase(
            UUID tenantId,
            TenantDatabaseRequest request
    );

    Map<String, Object> testConnection(UUID tenantId);

    Map<String, Object> getHealth(UUID tenantId);
}