package com.tenant_management.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "tenant_isolation",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_isolation_tenant",
                        columnNames = "tenant_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantIsolation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "tenant_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_tenant_isolation_tenant"
            )
    )
    private Tenant tenant;

    @Column(name = "database_isolation_enabled", nullable = false)
    private Boolean databaseIsolationEnabled;

    @Column(name = "storage_isolation_enabled", nullable = false)
    private Boolean storageIsolationEnabled;

    @Column(name = "api_tenant_only", nullable = false)
    private Boolean apiTenantOnly;

    @Column(name = "cross_tenant_access_enabled", nullable = false)
    private Boolean crossTenantAccessEnabled;

    @Column(name = "private_network_enabled", nullable = false)
    private Boolean privateNetworkEnabled;

    @Column(name = "ip_whitelisting_enabled", nullable = false)
    private Boolean ipWhitelistingEnabled;

    @Column(name = "allowed_ip_address", length = 255)
    private String allowedIpAddress;

    @Column(name = "database_isolation_status", length = 50)
    private String databaseIsolationStatus;

    @Column(name = "storage_isolation_status", length = 50)
    private String storageIsolationStatus;

    @Column(name = "api_security_status", length = 50)
    private String apiSecurityStatus;

    @Column(name = "compliance_status", length = 50)
    private String complianceStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}



