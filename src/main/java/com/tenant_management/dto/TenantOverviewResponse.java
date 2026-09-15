package com.tenant_management.dto;

public class TenantOverviewResponse {

    private long totalTenants;
    private long activeTenants;
    private long inactiveTenants;

    public TenantOverviewResponse() {
    }

    public TenantOverviewResponse(
            long totalTenants,
            long activeTenants,
            long inactiveTenants) {

        this.totalTenants = totalTenants;
        this.activeTenants = activeTenants;
        this.inactiveTenants = inactiveTenants;
    }

    public long getTotalTenants() {
        return totalTenants;
    }

    public void setTotalTenants(long totalTenants) {
        this.totalTenants = totalTenants;
    }

    public long getActiveTenants() {
        return activeTenants;
    }

    public void setActiveTenants(long activeTenants) {
        this.activeTenants = activeTenants;
    }

    public long getInactiveTenants() {
        return inactiveTenants;
    }

    public void setInactiveTenants(long inactiveTenants) {
        this.inactiveTenants = inactiveTenants;
    }
}