package com.tenant_management.dto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TenantBackupHistoryResponse {
    private UUID id;
    private UUID tenantId;
    private LocalDateTime backupDateTime;
    private String backupType;
    private String backupStatus;
    private String backupLocation;
    private String backupVersion;
}