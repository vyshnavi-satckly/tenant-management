package com.tenant_management.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenant_management.dto.TenantDatabaseRequest;
import com.tenant_management.dto.TenantDatabaseResponse;
import com.tenant_management.service.TenantDatabaseService;

@RestController
@RequestMapping("/api/tenants")
public class TenantDatabaseController {

    private final TenantDatabaseService tenantDatabaseService;

    public TenantDatabaseController(
            TenantDatabaseService tenantDatabaseService) {
        this.tenantDatabaseService = tenantDatabaseService;
    }

    @GetMapping("/{tenantId}/database")
    public ResponseEntity<TenantDatabaseResponse> getDatabase(
            @PathVariable UUID tenantId) {

        return ResponseEntity.ok(
                tenantDatabaseService.getDatabase(tenantId));
    }

    @PutMapping("/{tenantId}/database")
    public ResponseEntity<TenantDatabaseResponse> updateDatabase(
            @PathVariable UUID tenantId,
            @RequestBody TenantDatabaseRequest request) {

        return ResponseEntity.ok(
                tenantDatabaseService.updateDatabase(
                        tenantId,
                        request));
    }

    @PostMapping("/{tenantId}/database/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection(
            @PathVariable UUID tenantId) {

        return ResponseEntity.ok(
                tenantDatabaseService.testConnection(tenantId));
    }

    @GetMapping("/{tenantId}/database/health")
    public ResponseEntity<Map<String, Object>> getHealth(
            @PathVariable UUID tenantId) {

        return ResponseEntity.ok(
                tenantDatabaseService.getHealth(tenantId));
    }
}