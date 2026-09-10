package com.tenant_management.service;

import com.tenant_management.dto.*;
import java.util.List;
import java.util.UUID;

public interface TenantBackupService {

    TenantBackupResponse getBackupConfig(UUID tenantId);

    TenantBackupResponse updateBackupConfig(
            UUID tenantId,
            TenantBackupConfigRequest request,
            UUID updatedBy
    );

    TenantBackupHistoryResponse triggerManualBackup(
            UUID tenantId,
            UUID triggeredBy
    );

    List<TenantBackupHistoryResponse> getBackupHistory(UUID tenantId);

    TenantBackupHistoryResponse restoreBackup(
            UUID tenantId,
            String backupVersion,
            UUID restoredBy
    );

    List<TenantBackupHistoryResponse> getBackupLogs(UUID tenantId);
}