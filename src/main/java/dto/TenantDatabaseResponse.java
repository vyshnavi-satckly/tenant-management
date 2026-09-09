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
    private UUID createdBy;

    private LocalDateTime updatedAt;
    private UUID updatedBy;


    // Getter and Setter for id
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    // Getter and Setter for tenantId
    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }


    // Getter and Setter for databaseName
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


    // Getter and Setter for databaseType
    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }


    // Getter and Setter for serverName
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    // Getter and Setter for connectionStatus
    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }


    // Getter and Setter for allocatedStorageGb
    public BigDecimal getAllocatedStorageGb() {
        return allocatedStorageGb;
    }

    public void setAllocatedStorageGb(BigDecimal allocatedStorageGb) {
        this.allocatedStorageGb = allocatedStorageGb;
    }


    // Getter and Setter for usedStorageGb
    public BigDecimal getUsedStorageGb() {
        return usedStorageGb;
    }

    public void setUsedStorageGb(BigDecimal usedStorageGb) {
        this.usedStorageGb = usedStorageGb;
    }


    // Getter and Setter for availableStorageGb
    public BigDecimal getAvailableStorageGb() {
        return availableStorageGb;
    }

    public void setAvailableStorageGb(BigDecimal availableStorageGb) {
        this.availableStorageGb = availableStorageGb;
    }


    // Getter and Setter for cpuUsage
    public BigDecimal getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(BigDecimal cpuUsage) {
        this.cpuUsage = cpuUsage;
    }


    // Getter and Setter for memoryUsage
    public BigDecimal getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(BigDecimal memoryUsage) {
        this.memoryUsage = memoryUsage;
    }


    // Getter and Setter for lastBackup
    public LocalDateTime getLastBackup() {
        return lastBackup;
    }

    public void setLastBackup(LocalDateTime lastBackup) {
        this.lastBackup = lastBackup;
    }


    // Getter and Setter for autoBackupEnabled
    public Boolean getAutoBackupEnabled() {
        return autoBackupEnabled;
    }

    public void setAutoBackupEnabled(Boolean autoBackupEnabled) {
        this.autoBackupEnabled = autoBackupEnabled;
    }


    // Getter and Setter for maintenanceWindow
    public String getMaintenanceWindow() {
        return maintenanceWindow;
    }

    public void setMaintenanceWindow(String maintenanceWindow) {
        this.maintenanceWindow = maintenanceWindow;
    }


    // Getter and Setter for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    // Getter and Setter for createdBy
    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }


    // Getter and Setter for updatedAt
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    // Getter and Setter for updatedBy
    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }
}