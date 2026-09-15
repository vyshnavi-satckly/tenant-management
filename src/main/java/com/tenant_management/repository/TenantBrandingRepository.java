package com.tenant_management.repository;

import com.tenant_management.entity.Tenant;
import com.tenant_management.entity.TenantBranding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantBrandingRepository extends JpaRepository<TenantBranding, UUID> {

    Optional<TenantBranding> findByTenant(Tenant tenant);
}
