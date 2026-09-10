package com.tenant_management.service;

import com.tenant_management.dto.request.TenantBackupConfigRequestDto;
import com.tenant_management.dto.response.TenantBackupHistoryResponseDto;
import com.tenant_management.dto.response.TenantBackupResponseDto;

import java.util.List;
import java.util.UUID;

public interface TenantBackupService {
    TenantBackupResponseDto getBackupConfig(UUID tenantId);
    TenantBackupResponseDto updateBackupConfig(UUID tenantId, TenantBackupConfigRequestDto request, UUID updatedBy);
    TenantBackupHistoryResponseDto triggerManualBackup(UUID tenantId, UUID triggeredBy);
    List<TenantBackupHistoryResponseDto> getBackupHistory(UUID tenantId);
    TenantBackupHistoryResponseDto restoreBackup(UUID tenantId, String backupVersion, UUID restoredBy);
    List<TenantBackupHistoryResponseDto> getBackupLogs(UUID tenantId);
}