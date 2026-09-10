package com.tenant_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_backup_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantBackupHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "backup_date_time")
    private LocalDateTime backupDateTime;

    @Column(name = "backup_type", length = 30)
    private String backupType;

    @Column(name = "backup_status", length = 50)
    private String backupStatus;

    @Column(name = "backup_location", length = 500)
    private String backupLocation;

    @Column(name = "backup_version", length = 100)
    private String backupVersion;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}