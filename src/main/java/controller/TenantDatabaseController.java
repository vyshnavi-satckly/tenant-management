package com.tenant_management.controller;

import com.tenant_management.dto.TenantDatabaseRequest;
import com.tenant_management.dto.TenantDatabaseResponse;
import com.tenant_management.service.TenantDatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import com.tenant_management.dto.DatabaseConnectionResult;
import com.tenant_management.dto.TenantDatabaseHealthResponse;

@RestController
@RequestMapping("/api/tenants")
public class TenantDatabaseController {

    private final TenantDatabaseService tenantDatabaseService;

    public TenantDatabaseController(
            TenantDatabaseService tenantDatabaseService) {
        this.tenantDatabaseService = tenantDatabaseService;
    }

    @PostMapping("/{tenantId}/database/test-connection")
    public DatabaseConnectionResult testConnection(@PathVariable UUID tenantId,
            @RequestBody TenantDatabaseRequest request) {
        return tenantDatabaseService.testConnection(tenantId, request);
    }

    @GetMapping("/{tenantId}/database/health")
    public TenantDatabaseHealthResponse health(@PathVariable UUID tenantId) {
        return tenantDatabaseService.getHealth(tenantId);
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