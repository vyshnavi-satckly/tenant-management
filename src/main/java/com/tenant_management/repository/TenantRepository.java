package com.tenant_management.repository;

import com.tenant_management.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    Optional<Tenant> findByTenantId(String tenantId);
    boolean existsByTenantName(String tenantName);
    boolean existsByDomainName(String domainName);
    List<Tenant> findByIsDeletedFalse();

    // SEARCH - for 1A
    @Query("SELECT t FROM Tenant t WHERE t.isDeleted = false AND (" +
            "LOWER(t.tenantName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.organizationName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.tenantId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.domainName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Tenant> searchTenants(@Param("keyword") String keyword);

    // FILTER - for 1A
    List<Tenant> findByStatusAndIsDeletedFalse(String status);

    @Query("SELECT t FROM Tenant t WHERE t.isDeleted = false AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:subscriptionPlan IS NULL OR t.subscriptionPlan = :subscriptionPlan) AND " +
            "(:country IS NULL OR t.country = :country)")
    List<Tenant> filterTenants(@Param("status") String status,
                               @Param("subscriptionPlan") String subscriptionPlan,
                               @Param("country") String country);

    // OVERVIEW - for 1A
    long countByStatusAndIsDeletedFalse(String status);
    long countByIsDeletedFalse();
}