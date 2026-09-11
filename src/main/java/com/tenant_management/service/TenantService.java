package com.tenant_management.service;

import com.tenant_management.dto.request.CreateTenantRequest;
import com.tenant_management.dto.request.UpdateTenantRequest;
import com.tenant_management.dto.response.TenantResponse;
import com.tenant_management.entity.Tenant;
import java.util.List;
import java.util.Map;

public interface TenantService {
    Tenant createTenant(CreateTenantRequest req);
    Tenant updateTenant(String tenantId, UpdateTenantRequest req);
    Tenant enableTenant(String tenantId);
    Tenant disableTenant(String tenantId);
    List<Tenant> exportTenants();
    List<Tenant> getAllTenants();
    Tenant getTenantById(String tenantId);

    // TEAM 1A - APIs
    List<TenantResponse> searchTenants(String keyword);
    List<TenantResponse> filterTenants(String status, String subscriptionPlan, String country);
    Map<String, Object> getOverview();
}