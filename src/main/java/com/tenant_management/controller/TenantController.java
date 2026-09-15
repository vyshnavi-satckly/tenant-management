package com.tenant_management.controller;

import com.tenant_management.dto.request.CreateTenantRequest;
import com.tenant_management.dto.request.UpdateTenantRequest;
import com.tenant_management.dto.response.TenantResponse;
import com.tenant_management.entity.Tenant;
import com.tenant_management.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService service;
    public TenantController(TenantService service){this.service=service;}

    // ===== TEAM 1B APIs =====
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

    // ===== TEAM 1A APIs ====
    @GetMapping public ResponseEntity<List<Tenant>> getAll(){return ResponseEntity.ok(service.getAllTenants());}

    @GetMapping("/{tenantId}") public ResponseEntity<Tenant> getOne(@PathVariable String tenantId){return ResponseEntity.ok(service.getTenantById(tenantId));}

    @GetMapping("/search")
    public ResponseEntity<List<TenantResponse>> search(@RequestParam String q){
        return ResponseEntity.ok(service.searchTenants(q));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TenantResponse>> filter(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String subscriptionPlan,
            @RequestParam(required = false) String country){
        return ResponseEntity.ok(service.filterTenants(status, subscriptionPlan, country));
    }

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> overview(){
        return ResponseEntity.ok(service.getOverview());
    }
}