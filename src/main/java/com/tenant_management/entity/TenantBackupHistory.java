package com.tenant_management.entity;

import lombok.*;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID tenantId;
    private LocalDateTime backupDateTime;
    private String backupType;
    private String backupStatus;
    private String backupLocation;
    private String backupVersion;
    private LocalDateTime createdAt;
    private UUID createdBy;
}