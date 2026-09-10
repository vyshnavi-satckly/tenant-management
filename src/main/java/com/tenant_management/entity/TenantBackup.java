package com.tenant_management.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_backups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID tenantId;
    private Boolean autoBackupEnabled;
    private String backupFrequency;
    private LocalTime backupTime;
    private Integer retentionPeriodDays;
    private LocalDateTime lastBackup;
    private LocalDateTime nextScheduledBackup;
    private String currentStatus;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;
}