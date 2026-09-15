package com.tenant_management.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TenantBackupConfigRequestDto {

    @NotNull(message = "autoBackupEnabled is required")
    private Boolean autoBackupEnabled;

    @NotBlank(message = "Backup frequency is required")
    private String backupFrequency;

    @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalTime backupTime;

    @Min(value = 1, message = "Retention period must be greater than 0 days")
    private Integer retentionPeriodDays;
}