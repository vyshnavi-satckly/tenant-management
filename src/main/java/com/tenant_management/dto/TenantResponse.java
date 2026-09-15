package com.tenant_management.dto;

import java.time.LocalDateTime;

public class TenantResponse {

    private String tenantId;
    private String tenantName;
    private String subscriptionPlan;
    private String status;
    private LocalDateTime createdAt;

    public TenantResponse() {
    }

    public TenantResponse(String tenantId, String tenantName,
                          String subscriptionPlan, String status,
                          LocalDateTime createdAt) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.subscriptionPlan = subscriptionPlan;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(String subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}