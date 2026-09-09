package com.tenant_management.repository;

import com.tenant_management.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<TenantRepository, UUID> {

    Optional<Tenant> findByTenantId(String tenantId);
}
