package com.tenant_management.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class TenantResponse {
    private UUID id;
    private String tenantId;
    private String tenantName;
    private String organizationName;
    private String domainName;
    private String subscriptionPlan;
    private String status;
    private String primaryAdminName;
    private String primaryAdminEmail;
    private String primaryAdminMobile;
    private String country;
    private String timeZone;
    private String language;
    private LocalDateTime createdAt;

    // getters setters
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; } public void setTenantId(String t) { this.tenantId = t; }
    public String getTenantName() { return tenantName; } public void setTenantName(String t) { this.tenantName = t; }
    public String getOrganizationName() { return organizationName; } public void setOrganizationName(String o) { this.organizationName = o; }
    public String getDomainName() { return domainName; } public void setDomainName(String d) { this.domainName = d; }
    public String getSubscriptionPlan() { return subscriptionPlan; } public void setSubscriptionPlan(String s) { this.subscriptionPlan = s; }
    public String getStatus() { return status; } public void setStatus(String s) { this.status = s; }
    public String getPrimaryAdminName() { return primaryAdminName; } public void setPrimaryAdminName(String n) { this.primaryAdminName = n; }
    public String getPrimaryAdminEmail() { return primaryAdminEmail; } public void setPrimaryAdminEmail(String e) { this.primaryAdminEmail = e; }
    public String getPrimaryAdminMobile() { return primaryAdminMobile; } public void setPrimaryAdminMobile(String m) { this.primaryAdminMobile = m; }
    public String getCountry() { return country; } public void setCountry(String c) { this.country = c; }
    public String getTimeZone() { return timeZone; } public void setTimeZone(String t) { this.timeZone = t; }
    public String getLanguage() { return language; } public void setLanguage(String l) { this.language = l; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
}