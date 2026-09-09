package com.tenant_management.service;

import com.tenant_management.dto.TenantDatabaseRequest;
import com.tenant_management.dto.TenantDatabaseResponse;
import com.tenant_management.entity.Tenant;
import com.tenant_management.entity.TenantDatabase;
import com.tenant_management.repository.TenantDatabaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TenantDatabaseServiceImpl implements TenantDatabaseService {

    private final TenantDatabaseRepository tenantDatabaseRepository;

    public TenantDatabaseServiceImpl(
            TenantDatabaseRepository tenantDatabaseRepository) {
        this.tenantDatabaseRepository = tenantDatabaseRepository;
    }

    // CREATE database details
    @Override
    public TenantDatabaseResponse createDatabase(
            UUID tenantId,
            TenantDatabaseRequest request) {

        TenantDatabase tenantDatabase = new TenantDatabase();

        // Link database to existing tenant
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);

        tenantDatabase.setTenant(tenant);

        tenantDatabase.setDatabaseType(request.getDatabaseType());
        tenantDatabase.setServerName(request.getServerName());
        tenantDatabase.setAllocatedStorageGb(
                request.getAllocatedStorageGb());
        tenantDatabase.setAutoBackupEnabled(
                request.getAutoBackupEnabled());
        tenantDatabase.setMaintenanceWindow(
                request.getMaintenanceWindow());

        tenantDatabase.setConnectionStatus("CONNECTED");
        tenantDatabase.setCreatedAt(LocalDateTime.now());

        TenantDatabase savedDatabase =
                tenantDatabaseRepository.save(tenantDatabase);

        return convertToResponse(savedDatabase);
    }

    // GET database details
    @Override
    public TenantDatabaseResponse getDatabase(UUID tenantId) {

        TenantDatabase tenantDatabase = tenantDatabaseRepository
                .findAll()
                .stream()
                .filter(database ->
                        database.getTenant() != null
                                && database.getTenant().getId().equals(tenantId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Tenant database not found"));

        return convertToResponse(tenantDatabase);
    }

    // UPDATE database details
    @Override
    public TenantDatabaseResponse updateDatabase(
            UUID tenantId,
            TenantDatabaseRequest request) {

        TenantDatabase tenantDatabase = tenantDatabaseRepository
                .findAll()
                .stream()
                .filter(database ->
                        database.getTenant() != null
                                && database.getTenant().getId().equals(tenantId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Tenant database not found"));

        // Maintenance window is required
        if (request.getMaintenanceWindow() == null
                || request.getMaintenanceWindow().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Maintenance window is required");
        }

        // Allocated storage cannot be less than current usage
        if (request.getAllocatedStorageGb() != null
                && tenantDatabase.getUsedStorageGb() != null
                && request.getAllocatedStorageGb()
                        .compareTo(tenantDatabase.getUsedStorageGb()) < 0) {

            throw new IllegalArgumentException(
                    "Allocated storage cannot be less than current usage");
        }

        // Update allowed fields
        tenantDatabase.setDatabaseType(
                request.getDatabaseType());

        tenantDatabase.setServerName(
                request.getServerName());

        tenantDatabase.setAllocatedStorageGb(
                request.getAllocatedStorageGb());

        tenantDatabase.setAutoBackupEnabled(
                request.getAutoBackupEnabled());

        tenantDatabase.setMaintenanceWindow(
                request.getMaintenanceWindow());

        tenantDatabase.setUpdatedAt(LocalDateTime.now());

        TenantDatabase savedDatabase =
                tenantDatabaseRepository.save(tenantDatabase);

        return convertToResponse(savedDatabase);
    }

    // Convert Entity to Response DTO
    private TenantDatabaseResponse convertToResponse(
            TenantDatabase database) {

        TenantDatabaseResponse response =
                new TenantDatabaseResponse();

        response.setId(database.getId());

        if (database.getTenant() != null) {
            response.setTenantId(
                    database.getTenant().getId());
        }

        response.setDatabaseName(
                database.getDatabaseName());

        response.setDatabaseType(
                database.getDatabaseType());

        response.setServerName(
                database.getServerName());

        response.setConnectionStatus(
                database.getConnectionStatus());

        response.setAllocatedStorageGb(
                database.getAllocatedStorageGb());

        response.setUsedStorageGb(
                database.getUsedStorageGb());

        response.setAvailableStorageGb(
                database.getAvailableStorageGb());

        response.setCpuUsage(
                database.getCpuUsage());

        response.setMemoryUsage(
                database.getMemoryUsage());

        response.setLastBackup(
                database.getLastBackup());

        response.setAutoBackupEnabled(
                database.getAutoBackupEnabled());

        response.setMaintenanceWindow(
                database.getMaintenanceWindow());

        response.setCreatedAt(
                database.getCreatedAt());

        response.setCreatedBy(
                database.getCreatedBy());

        response.setUpdatedAt(
                database.getUpdatedAt());

        response.setUpdatedBy(
                database.getUpdatedBy());

        return response;
    }
}