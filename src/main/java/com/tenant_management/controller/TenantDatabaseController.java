package com.tenant_management.controller;

import com.tenant_management.dto.DatabaseConnectionResult;
import com.tenant_management.dto.TenantDatabaseHealthResponse;
import com.tenant_management.dto.TenantDatabaseResponse;
import com.tenant_management.dto.request.TenantDatabaseRequest;
import com.tenant_management.service.TenantDatabaseService;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenants")
public class TenantDatabaseController {
    private final TenantDatabaseService service;

    public TenantDatabaseController(TenantDatabaseService service) {
        this.service = service;
    }

    @PostMapping("/{tenantId}/database/test-connection")
    public DatabaseConnectionResult testConnection(@PathVariable UUID tenantId,
            @RequestBody TenantDatabaseRequest request) {
        return service.testConnection(tenantId, request);
    }

    @GetMapping("/{tenantId}/database/health")
    public TenantDatabaseHealthResponse getHealth(@PathVariable UUID tenantId) {
        return service.getHealth(tenantId);
    }

    @GetMapping("/{tenantId}/database")
    public TenantDatabaseResponse getDatabase(@PathVariable UUID tenantId) {
        return service.getDatabase(tenantId);
    }

    @PutMapping("/{tenantId}/database")
    public TenantDatabaseResponse updateDatabase(@PathVariable UUID tenantId,
            @RequestBody TenantDatabaseRequest request) {
        return service.updateDatabase(tenantId, request);
    }
}
