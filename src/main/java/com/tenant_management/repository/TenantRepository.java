package com.tenant_management.repository;

import com.tenant_management.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    boolean existsByTenantId(String tenantId);

    boolean existsByTenantName(String tenantName);

    boolean existsByDomainName(String domainName);
}