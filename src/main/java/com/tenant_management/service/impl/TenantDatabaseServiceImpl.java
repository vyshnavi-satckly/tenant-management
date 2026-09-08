package com.tenant_management.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tenant_management.dto.TenantDatabaseRequest;
import com.tenant_management.dto.TenantDatabaseResponse;
import com.tenant_management.entity.TenantDatabase;
import com.tenant_management.repository.TenantDatabaseRepository;
import com.tenant_management.service.TenantDatabaseService;

@Service
public class TenantDatabaseServiceImpl implements TenantDatabaseService {

    private final TenantDatabaseRepository tenantDatabaseRepository;

    public TenantDatabaseServiceImpl(
            TenantDatabaseRepository tenantDatabaseRepository) {
        this.tenantDatabaseRepository = tenantDatabaseRepository;
    }

    @Override
    public TenantDatabaseResponse getDatabase(UUID tenantId) {

        TenantDatabase entity = tenantDatabaseRepository
                .findByTenantId(tenantId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Tenant database not found for tenant: " + tenantId));

        return mapToResponse(entity);
    }

    @Override
    public TenantDatabaseResponse updateDatabase(
            UUID tenantId,
            TenantDatabaseRequest request) {

        TenantDatabase entity = tenantDatabaseRepository
                .findByTenantId(tenantId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Tenant database not found for tenant: " + tenantId));

        if (request.getMaintenanceWindow() == null
                || request.getMaintenanceWindow().isBlank()) {
            throw new IllegalArgumentException(
                    "Maintenance window is required");
        }

        if (request.getAllocatedStorageGb() != null
                && entity.getUsedStorageGb() != null
                && request.getAllocatedStorageGb()
                        .compareTo(entity.getUsedStorageGb()) < 0) {

            throw new IllegalArgumentException(
                    "Allocated storage cannot be less than current usage");
        }

        if (!"VERIFIED".equalsIgnoreCase(entity.getConnectionStatus())) {
            throw new IllegalStateException(
                    "Database connection must be verified before save");
        }

        entity.setDatabaseType(request.getDatabaseType());
        entity.setServerName(request.getServerName());
        entity.setAllocatedStorageGb(request.getAllocatedStorageGb());
        entity.setAutoBackupEnabled(request.getAutoBackupEnabled());
        entity.setMaintenanceWindow(request.getMaintenanceWindow());
        entity.setUpdatedAt(LocalDateTime.now());

        TenantDatabase saved =
                tenantDatabaseRepository.save(entity);

        return mapToResponse(saved);
    }

    @Override
    public Map<String, Object> testConnection(UUID tenantId) {

        TenantDatabase entity = tenantDatabaseRepository
                .findByTenantId(tenantId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Tenant database not found for tenant: " + tenantId));

        /*
         * Day 2:
         * Replace this with actual DB connection verification.
         */

        entity.setConnectionStatus("VERIFIED");
        entity.setUpdatedAt(LocalDateTime.now());

        tenantDatabaseRepository.save(entity);

        Map<String, Object> response = new HashMap<>();
        response.put("tenantId", tenantId);
        response.put("connected", true);
        response.put("connectionStatus", "VERIFIED");

        return response;
    }

    @Override
    public Map<String, Object> getHealth(UUID tenantId) {

        TenantDatabase entity = tenantDatabaseRepository
                .findByTenantId(tenantId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Tenant database not found for tenant: " + tenantId));

        Map<String, Object> health = new HashMap<>();

        health.put("tenantId", tenantId);
        health.put("connectionStatus", entity.getConnectionStatus());
        health.put("cpuUsage", entity.getCpuUsage());
        health.put("memoryUsage", entity.getMemoryUsage());
        health.put("allocatedStorageGb",
                entity.getAllocatedStorageGb());
        health.put("usedStorageGb",
                entity.getUsedStorageGb());
        health.put("availableStorageGb",
                entity.getAvailableStorageGb());

        return health;
    }

    private TenantDatabaseResponse mapToResponse(
            TenantDatabase entity) {

        TenantDatabaseResponse response =
                new TenantDatabaseResponse();

        response.setId(entity.getId());
        response.setTenantId(entity.getTenantId());

        response.setDatabaseName(entity.getDatabaseName());
        response.setDatabaseType(entity.getDatabaseType());
        response.setServerName(entity.getServerName());
        response.setConnectionStatus(
                entity.getConnectionStatus());

        response.setAllocatedStorageGb(
                entity.getAllocatedStorageGb());
        response.setUsedStorageGb(
                entity.getUsedStorageGb());
        response.setAvailableStorageGb(
                entity.getAvailableStorageGb());

        response.setCpuUsage(entity.getCpuUsage());
        response.setMemoryUsage(entity.getMemoryUsage());

        response.setLastBackup(entity.getLastBackup());
        response.setAutoBackupEnabled(
                entity.getAutoBackupEnabled());
        response.setMaintenanceWindow(
                entity.getMaintenanceWindow());

        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        return response;
    }
}