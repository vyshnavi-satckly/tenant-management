package com.tenant_management.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_databases")
public class TenantDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "tenant_id", nullable = false, unique = true)
    private Tenant tenant;

    @Column(name = "database_name", length = 150)
    private String databaseName;

    @Column(name = "database_type", length = 50)
    private String databaseType;

    @Column(name = "server_name", length = 255)
    private String serverName;

    @Column(name = "connection_status", length = 50)
    private String connectionStatus;

    @Column(name = "allocated_storage_gb")
    private BigDecimal allocatedStorageGb;

    @Column(name = "used_storage_gb")
    private BigDecimal usedStorageGb;

    @Column(name = "available_storage_gb")
    private BigDecimal availableStorageGb;

    @Column(name = "cpu_usage")
    private BigDecimal cpuUsage;

    @Column(name = "memory_usage")
    private BigDecimal memoryUsage;

    @Column(name = "last_backup")
    private LocalDateTime lastBackup;

    @Column(name = "auto_backup_enabled")
    private Boolean autoBackupEnabled;

    @Column(name = "maintenance_window", length = 100)
    private String maintenanceWindow;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;


    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
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

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }
}