package com.tenant_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",updatable = false,nullable = false)
    private UUID id;

    @Column(name = "tenant_id",nullable = false)
    private UUID tenantId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id",insertable = false,updatable = false)
    private Tenant tenant;

    @Column(name = "country",length = 120,nullable = false)
    private String country;

    @Column(name = "time_zone",length = 120,nullable = false)
    private String timeZone;

    @Column(name = "language", length = 100, nullable = false)
    private String language;

    @Column(name = "currency", length = 20, nullable = false)
    private String currency;

    @Column(name = "date_format", length = 50)
    private String dateFormat;

    @Column(name = "password_policy", length = 100, nullable = false)
    private String passwordPolicy;

    @Column(name = "session_timeout_minutes", nullable = false)
    private Integer sessionTimeoutMinutes;

    @Column(name = "mfa_enabled", nullable = false)
    private Boolean mfaEnabled;

    @Column(name = "storage_limit_gb", precision = 10, scale = 2, nullable = false)
    private BigDecimal storageLimitGb;

    @Column(name = "email_notifications_enabled", nullable = false)
    private Boolean emailNotificationsEnabled;

    @Column(name = "sms_notifications_enabled", nullable = false)
    private Boolean smsNotificationsEnabled;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @PrePersist
    protected void onCreated(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
