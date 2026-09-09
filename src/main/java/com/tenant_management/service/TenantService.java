package com.tenant_management.service;

import com.tenant_management.dto.TenantOverviewResponse;
import com.tenant_management.dto.TenantResponse;
import com.tenant_management.entity.Tenant;
import com.tenant_management.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    // Get all tenants
    public List<TenantResponse> getAllTenants() {
        return tenantRepository.findAll()
                .stream()
                .filter(tenant -> !tenant.isDeleted())
                .map(this::mapToResponse)
                .toList();
    }

    // Get tenant by Tenant ID
    public TenantResponse getTenantByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
                .filter(tenant -> !tenant.isDeleted())
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new RuntimeException("Tenant not found: " + tenantId));
    }

    // Search tenants by Tenant Name or Tenant ID
    public List<TenantResponse> searchTenants(String search) {
        return tenantRepository
                .findByTenantNameContainingIgnoreCaseOrTenantIdContainingIgnoreCase(
                        search,
                        search
                )
                .stream()
                .filter(tenant -> !tenant.isDeleted())
                .map(this::mapToResponse)
                .toList();
    }

    // Filter tenants by status
    public List<TenantResponse> filterByStatus(String status) {
        return tenantRepository.findByStatus(status)
                .stream()
                .filter(tenant -> !tenant.isDeleted())
                .map(this::mapToResponse)
                .toList();
    }

    // Filter tenants by subscription plan
    public List<TenantResponse> filterBySubscriptionPlan(String subscriptionPlan) {
        return tenantRepository.findBySubscriptionPlan(subscriptionPlan)
                .stream()
                .filter(tenant -> !tenant.isDeleted())
                .map(this::mapToResponse)
                .toList();
    }

    // Filter tenants by country / region
    public List<TenantResponse> filterByCountry(String country) {
        return tenantRepository.findByCountry(country)
                .stream()
                .filter(tenant -> !tenant.isDeleted())
                .map(this::mapToResponse)
                .toList();
    }

    // Get tenant overview
    public TenantOverviewResponse getTenantOverview() {

        List<Tenant> tenants = tenantRepository.findAll()
                .stream()
                .filter(tenant -> !tenant.isDeleted())
                .toList();

        long totalTenants = tenants.size();

        long activeTenants = tenants.stream()
                .filter(tenant -> "Active".equalsIgnoreCase(tenant.getStatus()))
                .count();

        long inactiveTenants = tenants.stream()
                .filter(tenant -> "Inactive".equalsIgnoreCase(tenant.getStatus()))
                .count();

        return new TenantOverviewResponse(
                totalTenants,
                activeTenants,
                inactiveTenants
        );
    }

    // Convert Tenant entity to TenantResponse
    private TenantResponse mapToResponse(Tenant tenant) {
        return new TenantResponse(
                tenant.getTenantId(),
                tenant.getTenantName(),
                tenant.getSubscriptionPlan(),
                tenant.getStatus(),
                tenant.getCreatedAt()
        );
    }
}