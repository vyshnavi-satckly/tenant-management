package com.tenant_management.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TenantBackupConfigRequestDto {
    private Boolean autoBackupEnabled;

    @NotBlank(message = "Backup frequency is required")
    private String backupFrequency;

    private LocalTime backupTime;

    @Min(value = 1, message = "Retention period must be greater than 0 days")
    private Integer retentionPeriodDays;
}