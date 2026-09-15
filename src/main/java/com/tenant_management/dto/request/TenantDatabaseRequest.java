package com.tenant_management.dto.request;

import java.math.BigDecimal;

public class TenantDatabaseRequest {

    private String databaseType;

    private String serverName;

    private BigDecimal allocatedStorageGb;

    private Boolean autoBackupEnabled;

    private String maintenanceWindow;

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public BigDecimal getAllocatedStorageGb() {
        return allocatedStorageGb;
    }

    public void setAllocatedStorageGb(BigDecimal allocatedStorageGb) {
        this.allocatedStorageGb = allocatedStorageGb;
    }

    public Boolean getAutoBackupEnabled() {
        return autoBackupEnabled;
    }

    public void setAutoBackupEnabled(Boolean autoBackupEnabled) {
        this.autoBackupEnabled = autoBackupEnabled;
    }

    public String getMaintenanceWindow() {
        return maintenanceWindow;
    }

    public void setMaintenanceWindow(String maintenanceWindow) {
        this.maintenanceWindow = maintenanceWindow;
    }
}
