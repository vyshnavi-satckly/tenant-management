package com.tenant_management.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "public")
public class Tenant {
    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @Column(name = "tenant_id", unique = true, nullable = false, length = 50)
    private String tenantId;
    @Column(name = "tenant_name", unique = true, nullable = false, length = 150)
    private String tenantName;
    @Column(name = "organization_name", nullable = false, length = 200)
    private String organizationName;
    @Column(name = "domain_name", unique = true, length = 255)
    private String domainName;
    @Column(name = "subscription_plan", nullable = false, length = 50)
    private String subscriptionPlan;
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    @Column(name = "primary_admin_name", nullable = false, length = 150)
    private String primaryAdminName;
    @Column(name = "primary_admin_email", nullable = false, length = 255)
    private String primaryAdminEmail;
    @Column(name = "primary_admin_mobile", nullable = false, length = 30)
    private String primaryAdminMobile;
    @Column(name = "country", nullable = false, length = 100)
    private String country;
    @Column(name = "time_zone", nullable = false, length = 100)
    private String timeZone;
    @Column(name = "language", nullable = false, length = 100)
    private String language;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "created_by") private UUID createdBy;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @Column(name = "updated_by") private UUID updatedBy;
    @Column(name = "is_deleted") private Boolean isDeleted = false;
    @Column(name = "deleted_at") private LocalDateTime deletedAt;
    @Column(name = "deleted_by") private UUID deletedBy;

    // Getters & Setters
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getTenantId(){return tenantId;} public void setTenantId(String v){this.tenantId=v;}
    public String getTenantName(){return tenantName;} public void setTenantName(String v){this.tenantName=v;}
    public String getOrganizationName(){return organizationName;} public void setOrganizationName(String v){this.organizationName=v;}
    public String getDomainName(){return domainName;} public void setDomainName(String v){this.domainName=v;}
    public String getSubscriptionPlan(){return subscriptionPlan;} public void setSubscriptionPlan(String v){this.subscriptionPlan=v;}
    public String getStatus(){return status;} public void setStatus(String v){this.status=v;}
    public String getPrimaryAdminName(){return primaryAdminName;} public void setPrimaryAdminName(String v){this.primaryAdminName=v;}
    public String getPrimaryAdminEmail(){return primaryAdminEmail;} public void setPrimaryAdminEmail(String v){this.primaryAdminEmail=v;}
    public String getPrimaryAdminMobile(){return primaryAdminMobile;} public void setPrimaryAdminMobile(String v){this.primaryAdminMobile=v;}
    public String getCountry(){return country;} public void setCountry(String v){this.country=v;}
    public String getTimeZone(){return timeZone;} public void setTimeZone(String v){this.timeZone=v;}
    public String getLanguage(){return language;} public void setLanguage(String v){this.language=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){this.createdAt=v;}
    public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime v){this.updatedAt=v;}
    public Boolean getIsDeleted(){return isDeleted;} public void setIsDeleted(Boolean v){this.isDeleted=v;}
}