package com.tenant_management.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenant_management.entity.TenantIsolation;

public interface TenantIsolationRepository
        extends JpaRepository<TenantIsolation, UUID> {

    Optional<TenantIsolation> findByTenantId(UUID tenantId);

    boolean existsByTenantId(UUID tenantId);
}
