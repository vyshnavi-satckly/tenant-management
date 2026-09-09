package com.tenant_management.controller;

import com.tenant_management.entity.Tenant;
import com.tenant_management.service.TenantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    // Create tenant
    @PostMapping
    public ResponseEntity<Tenant> createTenant(
            @RequestBody Tenant tenant) {

        Tenant createdTenant = tenantService.createTenant(tenant);

        return ResponseEntity.ok(createdTenant);
    }

    // Get all tenants
    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants() {

        return ResponseEntity.ok(
                tenantService.getAllTenants()
        );
    }

    // Get tenant by UUID
    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenantById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                tenantService.getTenantById(id)
        );
    }
}
