package com.tenant_management.controller;


import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenant_management.dto.request.TenantIsolationRequestDto;
import com.tenant_management.dto.response.TenantIsolationResponseDto;
import com.tenant_management.dto.response.TenantIsolationStatusResponseDto;
import com.tenant_management.service.TenantIsolationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tenants")
public class TenantIsolationController {


    private final TenantIsolationService tenantIsolationService;


    public TenantIsolationController(
            TenantIsolationService tenantIsolationService) {

        this.tenantIsolationService =
                tenantIsolationService;
    }


    /*
     * GET isolation configuration
     *
     * GET
     * /api/tenants/{tenantId}/isolation
     */

    @GetMapping("/{tenantId}/isolation")
    public ResponseEntity<TenantIsolationResponseDto>
    getIsolation(
            @PathVariable UUID tenantId) {

        TenantIsolationResponseDto response =
                tenantIsolationService
                        .getIsolation(tenantId);

        return ResponseEntity.ok(response);
    }


    /*
     * UPDATE isolation configuration
     *
     * PUT
     * /api/tenants/{tenantId}/isolation
     */

    @PutMapping("/{tenantId}/isolation")
    public ResponseEntity<TenantIsolationResponseDto>
    updateIsolation(
            @PathVariable UUID tenantId,
            @Valid @RequestBody
            TenantIsolationRequestDto request) {


        TenantIsolationResponseDto response =
                tenantIsolationService
                        .updateIsolation(
                                tenantId,
                                request
                        );


        return ResponseEntity.ok(response);
    }


    /*
     * GET isolation status
     *
     * GET
     * /api/tenants/{tenantId}/isolation/status
     */

    @GetMapping("/{tenantId}/isolation/status")
    public ResponseEntity<TenantIsolationStatusResponseDto>
    getIsolationStatus(
            @PathVariable UUID tenantId) {


        TenantIsolationStatusResponseDto response =
                tenantIsolationService
                        .getIsolationStatus(tenantId);


        return ResponseEntity.ok(response);
    }
}
