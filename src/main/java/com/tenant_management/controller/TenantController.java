package com.tenant_management.controller;

import com.tenant_management.dto.TenantOverviewResponse;
import com.tenant_management.dto.TenantResponse;
import com.tenant_management.service.TenantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    // GET /api/tenants
    // Get all tenants
    @GetMapping
    public List<TenantResponse> getAllTenants() {
        return tenantService.getAllTenants();
    }

    // GET /api/tenants/{tenantId}
    // Get tenant by Tenant ID
    @GetMapping("/{tenantId}")
    public TenantResponse getTenantByTenantId(
            @PathVariable String tenantId) {

        return tenantService.getTenantByTenantId(tenantId);
    }

    // GET /api/tenants/search?query=value
    // Search by Tenant Name or Tenant ID
    @GetMapping("/search")
    public List<TenantResponse> searchTenants(
            @RequestParam String query) {

        return tenantService.searchTenants(query);
    }

    // GET /api/tenants/filter?status=Active
    // GET /api/tenants/filter?subscriptionPlan=Premium
    // GET /api/tenants/filter?country=India
    @GetMapping("/filter")
    public List<TenantResponse> filterTenants(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String subscriptionPlan,
            @RequestParam(required = false) String country) {

        if (status != null && !status.isBlank()) {
            return tenantService.filterByStatus(status);
        }

        if (subscriptionPlan != null && !subscriptionPlan.isBlank()) {
            return tenantService.filterBySubscriptionPlan(subscriptionPlan);
        }

        if (country != null && !country.isBlank()) {
            return tenantService.filterByCountry(country);
        }

        return tenantService.getAllTenants();
    }

    // GET /api/tenants/overview
    // Get total, active and inactive tenant counts
    @GetMapping("/overview")
    public TenantOverviewResponse getTenantOverview() {
        return tenantService.getTenantOverview();
    }
}