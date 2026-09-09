package com.tenant_management.repository;

import com.tenant_management.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    Optional<Tenant> findByTenantId(String tenantId);

    List<Tenant> findByTenantNameContainingIgnoreCaseOrTenantIdContainingIgnoreCase(
            String tenantName,
            String tenantId
    );

    List<Tenant> findByStatus(String status);

    List<Tenant> findBySubscriptionPlan(String subscriptionPlan);

    List<Tenant> findByCountry(String country);
}