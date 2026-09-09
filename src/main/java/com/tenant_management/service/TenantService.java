package com.tenant_management.service;

import com.tenant_management.dto.request.CreateTenantRequest;
import com.tenant_management.dto.request.UpdateTenantRequest;
import com.tenant_management.entity.Tenant;
import java.util.List;

public interface TenantService {
    Tenant createTenant(CreateTenantRequest req);
    Tenant updateTenant(String tenantId, UpdateTenantRequest req);
    Tenant enableTenant(String tenantId);
    Tenant disableTenant(String tenantId);
    List<Tenant> exportTenants();
    List<Tenant> getAllTenants();
    Tenant getTenantById(String tenantId);
}