package com.tenant_management.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TenantDatabaseResponse {

    private UUID id;
    private UUID tenantId;

    private String databaseName;
    private String databaseType;
    private String serverName;
    private String connectionStatus;

    private BigDecimal allocatedStorageGb;
    private BigDecimal usedStorageGb;
    private BigDecimal availableStorageGb;

    private BigDecimal cpuUsage;
    private BigDecimal memoryUsage;

    private LocalDateTime lastBackup;
    private Boolean autoBackupEnabled;
    private String maintenanceWindow;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TenantDatabaseResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

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

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public BigDecimal getAllocatedStorageGb() {
        return allocatedStorageGb;
    }

    public void setAllocatedStorageGb(BigDecimal allocatedStorageGb) {
        this.allocatedStorageGb = allocatedStorageGb;
    }

    public BigDecimal getUsedStorageGb() {
        return usedStorageGb;
    }

    public void setUsedStorageGb(BigDecimal usedStorageGb) {
        this.usedStorageGb = usedStorageGb;
    }

    public BigDecimal getAvailableStorageGb() {
        return availableStorageGb;
    }

    public void setAvailableStorageGb(BigDecimal availableStorageGb) {
        this.availableStorageGb = availableStorageGb;
    }

    public BigDecimal getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(BigDecimal cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public BigDecimal getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(BigDecimal memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public LocalDateTime getLastBackup() {
        return lastBackup;
    }

    public void setLastBackup(LocalDateTime lastBackup) {
        this.lastBackup = lastBackup;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}