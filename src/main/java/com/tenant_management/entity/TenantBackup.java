package com.tenant_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_backups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantBackup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "auto_backup_enabled")
    private Boolean autoBackupEnabled;

    @Column(name = "backup_frequency", length = 30)
    private String backupFrequency;

    @Column(name = "backup_time")
    private LocalTime backupTime;

    @Column(name = "retention_period_days")
    private Integer retentionPeriodDays;

    @Column(name = "last_backup")
    private LocalDateTime lastBackup;

    @Column(name = "next_scheduled_backup")
    private LocalDateTime nextScheduledBackup;

    @Column(name = "current_status", length = 50)
    private String currentStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}