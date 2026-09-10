package com.tenant_management.dto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TenantBackupResponse {
    private UUID id;
    private UUID tenantId;
    private Boolean autoBackupEnabled;
    private String backupFrequency;
    private LocalTime backupTime;
    private Integer retentionPeriodDays;
    private LocalDateTime lastBackup;
    private LocalDateTime nextScheduledBackup;
    private String currentStatus;
}