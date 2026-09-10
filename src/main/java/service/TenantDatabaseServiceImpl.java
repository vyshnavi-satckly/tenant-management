package com.tenant_management.service;

import com.tenant_management.dto.*;
import com.tenant_management.entity.*;
import com.tenant_management.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class TenantDatabaseServiceImpl implements TenantDatabaseService {
    private final TenantDatabaseRepository tenantDatabaseRepository;
    private final TenantRepository tenants;
    private final DatabaseConnectionVerifier verifier;
    private final ApplicationEventPublisher events;

    public TenantDatabaseServiceImpl(TenantDatabaseRepository databases, TenantRepository tenants,
            DatabaseConnectionVerifier verifier, ApplicationEventPublisher events) {
        this.tenantDatabaseRepository = databases;
        this.tenants = tenants;
        this.verifier = verifier;
        this.events = events;
    }

    private Tenant requireTenant(UUID id) {
        if (id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tenant is required");
        return tenants.findById(id).filter(t -> !Boolean.TRUE.equals(t.getIsDeleted()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tenant not found"));
    }

    private TenantDatabase requireDatabase(UUID id) {
        requireTenant(id);
        return tenantDatabaseRepository.findByTenant_Id(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tenant database not found"));
    }

    private String databaseName(UUID tenantId) {
        return "tenant_" + tenantId.toString().replace("-", "");
    }

    private void validate(TenantDatabaseRequest request, BigDecimal used) {
        if (request == null || request.getMaintenanceWindow() == null
                || request.getMaintenanceWindow().isBlank() || request.getMaintenanceWindow().length() > 100)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maintenance window is required (max 100 characters)");
        if (request.getAllocatedStorageGb() == null || request.getAllocatedStorageGb().signum() <= 0
                || request.getAllocatedStorageGb().compareTo(used) < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allocated storage must be positive and at least current usage");
        if (request.getAutoBackupEnabled() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auto backup setting is required");
        verifier.validateTarget(request.getDatabaseType(), request.getServerName());
    }

    @Override
    public TenantDatabaseResponse createDatabase(UUID tenantId, TenantDatabaseRequest request) {
        Tenant tenant = requireTenant(tenantId);
        if (tenantDatabaseRepository.findByTenant_Id(tenantId).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tenant database already exists");
        TenantDatabase database = new TenantDatabase();
        database.setTenant(tenant);
        database.setDatabaseName(databaseName(tenantId));
        database.setUsedStorageGb(BigDecimal.ZERO);
        database.setCreatedAt(LocalDateTime.now());
        return save(database, request, "DATABASE_CREATED");
    }

    @Override
    public TenantDatabaseResponse getDatabase(UUID tenantId) {
        TenantDatabaseResponse response = convertToResponse(requireDatabase(tenantId));
        events.publishEvent(new TenantDatabaseAuditEvent(tenantId, "DATABASE_VIEWED", true, LocalDateTime.now()));
        return response;
    }

    @Override
    public TenantDatabaseResponse updateDatabase(UUID tenantId, TenantDatabaseRequest request) {
        requireTenant(tenantId);
        TenantDatabase database = tenantDatabaseRepository.findByTenant_Id(tenantId).orElse(null);
        if (database == null) return createDatabase(tenantId, request);
        return save(database, request, "DATABASE_UPDATED");
    }

    private TenantDatabaseResponse save(TenantDatabase database, TenantDatabaseRequest request, String action) {
        BigDecimal used = database.getUsedStorageGb() == null ? BigDecimal.ZERO : database.getUsedStorageGb();
        validate(request, used);
        String name = database.getDatabaseName() == null
                ? databaseName(database.getTenant().getId()) : database.getDatabaseName();
        DatabaseConnectionResult result = verifier.verify(request.getDatabaseType(), request.getServerName(), name);
        if (!result.connected())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Connection verification failed; database settings were not saved");
        database.setDatabaseName(name);
        database.setDatabaseType(request.getDatabaseType().trim().toUpperCase(java.util.Locale.ROOT));
        database.setServerName(request.getServerName().trim());
        database.setAllocatedStorageGb(request.getAllocatedStorageGb());
        database.setUsedStorageGb(used);
        database.setAvailableStorageGb(request.getAllocatedStorageGb().subtract(used));
        database.setAutoBackupEnabled(request.getAutoBackupEnabled());
        database.setMaintenanceWindow(request.getMaintenanceWindow().trim());
        database.setConnectionStatus("CONNECTED");
        database.setUpdatedAt(LocalDateTime.now());
        TenantDatabase saved = tenantDatabaseRepository.save(database);
        events.publishEvent(new TenantDatabaseAuditEvent(database.getTenant().getId(), action, true, LocalDateTime.now()));
        return convertToResponse(saved);
    }

    @Override
    public DatabaseConnectionResult testConnection(UUID tenantId, TenantDatabaseRequest request) {
        requireTenant(tenantId);
        if (request == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Connection settings are required");
        String name = tenantDatabaseRepository.findByTenant_Id(tenantId).map(TenantDatabase::getDatabaseName)
                .orElse(databaseName(tenantId));
        DatabaseConnectionResult result = verifier.verify(request.getDatabaseType(), request.getServerName(), name);
        events.publishEvent(new TenantDatabaseAuditEvent(tenantId, "DATABASE_CONNECTION_TESTED", result.connected(), LocalDateTime.now()));
        return result;
    }

    @Override
    public TenantDatabaseHealthResponse getHealth(UUID tenantId) {
        TenantDatabase database = requireDatabase(tenantId);
        DatabaseConnectionResult connection = verifier.verify(database.getDatabaseType(), database.getServerName(), database.getDatabaseName());
        events.publishEvent(new TenantDatabaseAuditEvent(tenantId, "DATABASE_HEALTH_CHECKED", connection.connected(), LocalDateTime.now()));
        return new TenantDatabaseHealthResponse(tenantId, database.getDatabaseName(), connection,
                database.getAllocatedStorageGb(), database.getUsedStorageGb(), database.getAvailableStorageGb(),
                database.getCpuUsage(), database.getMemoryUsage());
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
