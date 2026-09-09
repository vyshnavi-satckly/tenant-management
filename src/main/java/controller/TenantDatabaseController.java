package com.tenant_management.controller;

import com.tenant_management.dto.TenantDatabaseRequest;
import com.tenant_management.dto.TenantDatabaseResponse;
import com.tenant_management.service.TenantDatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
public class TenantDatabaseController {

    private final TenantDatabaseService tenantDatabaseService;

    public TenantDatabaseController(
            TenantDatabaseService tenantDatabaseService) {
        this.tenantDatabaseService = tenantDatabaseService;
    }

    // CREATE database details
    @PostMapping("/{tenantId}/database")
    public ResponseEntity<TenantDatabaseResponse> createDatabase(
            @PathVariable UUID tenantId,
            @RequestBody TenantDatabaseRequest request) {

        TenantDatabaseResponse response =
                tenantDatabaseService.createDatabase(
                        tenantId, request);

        return ResponseEntity.ok(response);
    }

    // GET database details
    @GetMapping("/{tenantId}/database")
    public ResponseEntity<TenantDatabaseResponse> getDatabase(
            @PathVariable UUID tenantId) {

        TenantDatabaseResponse response =
                tenantDatabaseService.getDatabase(tenantId);

        return ResponseEntity.ok(response);
    }

    // UPDATE database details
    @PutMapping("/{tenantId}/database")
    public ResponseEntity<TenantDatabaseResponse> updateDatabase(
            @PathVariable UUID tenantId,
            @RequestBody TenantDatabaseRequest request) {

        TenantDatabaseResponse response =
                tenantDatabaseService.updateDatabase(
                        tenantId, request);

        return ResponseEntity.ok(response);
    }
}