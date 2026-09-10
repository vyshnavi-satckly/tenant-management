package com.tenant_management.service;

import com.tenant_management.entity.Tenant;

import java.util.List;
import java.util.UUID;

public interface TenantService {

    Tenant createTenant(Tenant tenant);

    List<Tenant> getAllTenants();

    Tenant getTenantById(UUID id);
}