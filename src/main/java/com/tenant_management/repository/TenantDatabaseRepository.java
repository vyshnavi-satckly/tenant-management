package com.tenant_management.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tenant_management.entity.TenantDatabase;

@Repository
public interface TenantDatabaseRepository
        extends JpaRepository<TenantDatabase, UUID> {

    Optional<TenantDatabase> findByTenantId(UUID tenantId);
}