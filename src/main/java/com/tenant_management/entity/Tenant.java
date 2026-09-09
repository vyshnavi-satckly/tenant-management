package com.tenant_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tenants", schema = "public")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
        name = "id",
        nullable = false,
        updatable = false
    )
    private UUID id;

    @Column(
        name = "tenant_id",
        nullable = false,
        unique = true,
        length = 50,
        updatable = false
    )
    private String tenantId;

    @Column(
        name = "tenant_name",
        nullable = false,
        unique = true,
        length = 150
    )
    private String tenantName;

    @Column(
        name = "organization_name",
        nullable = false,
        length = 200
    )
    private String organizationName;

    @Column(
        name = "domain_name",
        unique = true,
        length = 255
    )
    private String domainName;

    @Column(
        name = "subscription_plan",
        nullable = false,
        length = 50
    )
    private String subscriptionPlan;

    @Column(
        name = "status",
        nullable = false,
        length = 20
    )
    private String status;

    @Column(
        name = "primary_admin_name",
        nullable = false,
        length = 150
    )
    private String primaryAdminName;

    @Column(
        name = "primary_admin_email",
        nullable = false,
        length = 255
    )
    private String primaryAdminEmail;

    @Column(
        name = "primary_admin_mobile",
        nullable = false,
        length = 30
    )
    private String primaryAdminMobile;

    @Column(
        name = "country",
        nullable = false,
        length = 100
    )
    private String country;

    @Column(
        name = "time_zone",
        nullable = false,
        length = 100
    )
    private String timeZone;

    @Column(
        name = "language",
        nullable = false,
        length = 100
    )
    private String language;

    @Column(
        name = "created_at",
        updatable = false
    )
    private Instant createdAt;

    @Column(
        name = "created_by",
        updatable = false
    )
    private UUID createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(
        name = "is_deleted",
        nullable = false
    )
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;
}
