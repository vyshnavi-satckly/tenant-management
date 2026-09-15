package com.tenant_management.controller;

import com.tenant_management.dto.request.TenantBackupConfigRequestDto;
import com.tenant_management.dto.response.TenantBackupHistoryResponseDto;
import com.tenant_management.dto.response.TenantBackupResponseDto;
import com.tenant_management.service.TenantBackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantBackupController {

    private final TenantBackupService backupService;

    @GetMapping("/{tenantId}/backup/config")
    public ResponseEntity<TenantBackupResponseDto> getBackupConfig(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(backupService.getBackupConfig(tenantId));
    }

    @PutMapping("/{tenantId}/backup/config")
    public ResponseEntity<TenantBackupResponseDto> updateBackupConfig(
            @PathVariable UUID tenantId,
            @RequestBody TenantBackupConfigRequestDto request,
            @RequestHeader("X-User-Id") UUID updatedBy) {
        return ResponseEntity.ok(backupService.updateBackupConfig(tenantId, request, updatedBy));
    }

    @PostMapping("/{tenantId}/backup/trigger")
    public ResponseEntity<TenantBackupHistoryResponseDto> triggerManualBackup(
            @PathVariable UUID tenantId,
            @RequestHeader("X-User-Id") UUID triggeredBy) {
        return ResponseEntity.ok(backupService.triggerManualBackup(tenantId, triggeredBy));
    }

    @GetMapping("/{tenantId}/backup/history")
    public ResponseEntity<List<TenantBackupHistoryResponseDto>> getBackupHistory(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(backupService.getBackupHistory(tenantId));
    }

    @PostMapping("/{tenantId}/backup/restore")
    public ResponseEntity<TenantBackupHistoryResponseDto> restoreBackup(
            @PathVariable UUID tenantId,
            @RequestParam String backupVersion,
            @RequestHeader("X-User-Id") UUID restoredBy) {
        return ResponseEntity.ok(backupService.restoreBackup(tenantId, backupVersion, restoredBy));
    }

    @GetMapping("/{tenantId}/backup/logs")
    public ResponseEntity<List<TenantBackupHistoryResponseDto>> getBackupLogs(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(backupService.getBackupLogs(tenantId));
    }
}