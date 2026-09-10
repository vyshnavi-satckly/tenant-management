package com.tenant_management.controller;

import com.tenant_management.dto.request.CreateTenantRequest;
import com.tenant_management.dto.request.UpdateTenantRequest;
import com.tenant_management.entity.Tenant;
import com.tenant_management.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService service;
    public TenantController(TenantService service){this.service=service;}

    @PostMapping public ResponseEntity<Tenant> create(@Valid @RequestBody CreateTenantRequest req){
        return ResponseEntity.ok(service.createTenant(req));
    }
    @PutMapping("/{tenantId}") public ResponseEntity<Tenant> update(@PathVariable String tenantId, @RequestBody UpdateTenantRequest req){
        return ResponseEntity.ok(service.updateTenant(tenantId, req));
    }
    @PutMapping("/{tenantId}/enable") public ResponseEntity<Tenant> enable(@PathVariable String tenantId){
        return ResponseEntity.ok(service.enableTenant(tenantId));
    }
    @PutMapping("/{tenantId}/disable") public ResponseEntity<Tenant> disable(@PathVariable String tenantId){
        return ResponseEntity.ok(service.disableTenant(tenantId));
    }
    @GetMapping("/export") public ResponseEntity<List<Tenant>> export(){
        return ResponseEntity.ok(service.exportTenants());
    }
    // Team 1A APIs for testing
    @GetMapping public ResponseEntity<List<Tenant>> getAll(){return ResponseEntity.ok(service.getAllTenants());}
    @GetMapping("/{tenantId}") public ResponseEntity<Tenant> getOne(@PathVariable String tenantId){return ResponseEntity.ok(service.getTenantById(tenantId));}
}