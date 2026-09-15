package com.tenant_management.repository;

import com.tenant_management.entity.TenantConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TenantConfigurationRepository extends JpaRepository<TenantConfiguration, UUID> {

    Optional<TenantConfiguration> findByTenantId(UUID tenantId);

    boolean existsByTenantId(UUID tenantId);

}
