package com.tenant_management.controller;

import com.tenant_management.dto.request.TenantConfigurationRequestDto;
import com.tenant_management.dto.response.TenantConfigurationResponseDto;
import com.tenant_management.service.TenantConfigurationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
public class TenantConfigurationController {

    private final TenantConfigurationService tenantConfigurationService;

    public TenantConfigurationController(
            TenantConfigurationService tenantConfigurationService) {
        this.tenantConfigurationService = tenantConfigurationService;
    }

    @GetMapping("/{tenantId}/configuration")
    public ResponseEntity<TenantConfigurationResponseDto> getConfiguration(
            @PathVariable UUID tenantId) {

        return ResponseEntity.ok(
                tenantConfigurationService.getConfiguration(tenantId)
        );
    }

    @PutMapping("/{tenantId}/configuration")
    public ResponseEntity<TenantConfigurationResponseDto> updateConfiguration(
            @PathVariable UUID tenantId,
            @Valid @RequestBody TenantConfigurationRequestDto request,
            @RequestHeader(value = "X-User-Id", required = false) UUID updatedBy) {

        return ResponseEntity.ok(
                tenantConfigurationService.updateConfiguration(
                        tenantId,
                        request,
                        updatedBy
                )
        );
    }
}