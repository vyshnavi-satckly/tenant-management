package com.tenant_management.controller;

import com.tenant_management.dto.*;
import com.tenant_management.service.TenantBackupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants/{tenantId}/backup")
@RequiredArgsConstructor
public class TenantBackupController {

    private final TenantBackupService backupService;

    @GetMapping
    public ResponseEntity<TenantBackupResponse> getBackupConfig(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(backupService.getBackupConfig(tenantId));
    }

    @PutMapping
    public ResponseEntity<TenantBackupResponse> updateBackupConfig(
            @PathVariable UUID tenantId,
            @Valid @RequestBody TenantBackupConfigRequest request,
            @RequestHeader(value = "X-User-Id", required = false) UUID updatedBy) {
        return ResponseEntity.ok(backupService.updateBackupConfig(tenantId, request, updatedBy));
    }

    @PostMapping
    public ResponseEntity<TenantBackupHistoryResponse> triggerBackup(
            @PathVariable UUID tenantId,
            @RequestHeader(value = "X-User-Id", required = false) UUID triggeredBy) {
        return ResponseEntity.ok(backupService.triggerManualBackup(tenantId, triggeredBy));
    }

    @GetMapping("/history")
    public ResponseEntity<List<TenantBackupHistoryResponse>> getBackupHistory(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(backupService.getBackupHistory(tenantId));
    }

    @PostMapping("/restore/{backupVersion}")
    public ResponseEntity<TenantBackupHistoryResponse> restoreBackup(
            @PathVariable UUID tenantId,
            @PathVariable String backupVersion,
            @RequestHeader(value = "X-User-Id", required = false) UUID restoredBy) {
        return ResponseEntity.ok(backupService.restoreBackup(tenantId, backupVersion, restoredBy));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<TenantBackupHistoryResponse>> getBackupLogs(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(backupService.getBackupLogs(tenantId));
    }
}