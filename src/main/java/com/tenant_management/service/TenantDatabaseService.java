package com.tenant_management.service;

import com.tenant_management.dto.*;
import com.tenant_management.dto.request.TenantDatabaseRequest;
import com.tenant_management.entity.*;
import com.tenant_management.repository.*;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class TenantDatabaseService {
    private static final String SHARED_DATABASE_NAME = "cloud_platform";
    private final TenantDatabaseRepository tenantDatabaseRepository;
    private final TenantRepository tenants;
    private final DataSource dataSource;
    private final JdbcTemplate jdbc;
    private final String configuredServer;

    public TenantDatabaseService(TenantDatabaseRepository databases, TenantRepository tenants,
            DataSource dataSource, JdbcTemplate jdbc,
            @Value("${spring.datasource.url}") String datasourceUrl) {
        this.tenantDatabaseRepository = databases;
        this.tenants = tenants;
        this.dataSource = dataSource;
        this.jdbc = jdbc;
        this.configuredServer = serverFromJdbcUrl(datasourceUrl);
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

    private TenantDatabase useSharedDatabase(TenantDatabase database) {
        if (!SHARED_DATABASE_NAME.equals(database.getDatabaseName())) {
            database.setDatabaseName(SHARED_DATABASE_NAME);
            return tenantDatabaseRepository.save(database);
        }
        return database;
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
        validateTarget(request.getDatabaseType(), request.getServerName());
    }

    private TenantDatabaseResponse createDatabase(UUID tenantId, TenantDatabaseRequest request) {
        Tenant tenant = requireTenant(tenantId);
        if (tenantDatabaseRepository.findByTenant_Id(tenantId).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tenant database already exists");
        TenantDatabase database = new TenantDatabase();
        database.setTenant(tenant);
        database.setDatabaseName(SHARED_DATABASE_NAME);
        database.setUsedStorageGb(BigDecimal.ZERO);
        database.setCreatedAt(LocalDateTime.now());
        return save(database, request, "DATABASE_CREATED");
    }

    public TenantDatabaseResponse getDatabase(UUID tenantId) {
        TenantDatabaseResponse response = convertToResponse(useSharedDatabase(requireDatabase(tenantId)));
        recordAudit(tenantId, "DATABASE_VIEWED", true);
        return response;
    }

    public TenantDatabaseResponse updateDatabase(UUID tenantId, TenantDatabaseRequest request) {
        requireTenant(tenantId);
        TenantDatabase database = tenantDatabaseRepository.findByTenant_Id(tenantId).orElse(null);
        if (database == null) return createDatabase(tenantId, request);
        return save(database, request, "DATABASE_UPDATED");
    }

    private TenantDatabaseResponse save(TenantDatabase database, TenantDatabaseRequest request, String action) {
        BigDecimal used = database.getUsedStorageGb() == null ? BigDecimal.ZERO : database.getUsedStorageGb();
        validate(request, used);
        DatabaseConnectionResult result = verifyConnection(request.getDatabaseType(), request.getServerName());
        if (!result.connected())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Connection verification failed; database settings were not saved");
        database.setDatabaseName(SHARED_DATABASE_NAME);
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
        recordAudit(database.getTenant().getId(), action, true);
        return convertToResponse(saved);
    }

    public DatabaseConnectionResult testConnection(UUID tenantId, TenantDatabaseRequest request) {
        requireTenant(tenantId);
        if (request == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Connection settings are required");
        DatabaseConnectionResult result = verifyConnection(request.getDatabaseType(), request.getServerName());
        recordAudit(tenantId, "DATABASE_CONNECTION_TESTED", result.connected());
        return result;
    }

    public TenantDatabaseHealthResponse getHealth(UUID tenantId) {
        TenantDatabase database = useSharedDatabase(requireDatabase(tenantId));
        DatabaseConnectionResult connection = verifyConnection(database.getDatabaseType(), database.getServerName());
        recordAudit(tenantId, "DATABASE_HEALTH_CHECKED", connection.connected());
        return new TenantDatabaseHealthResponse(tenantId, database.getDatabaseName(), connection,
                database.getAllocatedStorageGb(), database.getUsedStorageGb(), database.getAvailableStorageGb(),
                database.getCpuUsage(), database.getMemoryUsage());
    }

    private void validateTarget(String type, String server) {
        if (type == null || !"POSTGRESQL".equalsIgnoreCase(type.trim()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supported database type is POSTGRESQL");
        if (server == null || !configuredServer.equalsIgnoreCase(server.trim()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Server must match the configured shared database host:port");
    }

    private DatabaseConnectionResult verifyConnection(String type, String server) {
        validateTarget(type, server);
        try (Connection connection = dataSource.getConnection()) {
            boolean connected = connection.isValid(5);
            return connectionResult(connected, "Connection check completed");
        } catch (SQLException exception) {
            return connectionResult(false, "Unable to connect to the shared database");
        }
    }

    private DatabaseConnectionResult connectionResult(boolean connected, String message) {
        return new DatabaseConnectionResult(connected, connected ? "CONNECTED" : "DISCONNECTED",
                message, LocalDateTime.now());
    }

    private String serverFromJdbcUrl(String datasourceUrl) {
        try {
            URI uri = URI.create(datasourceUrl.substring("jdbc:".length()));
            if (!"postgresql".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null)
                throw new IllegalArgumentException();
            return uri.getHost() + ":" + (uri.getPort() == -1 ? 5432 : uri.getPort());
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("spring.datasource.url must be a PostgreSQL JDBC URL", exception);
        }
    }

    private void recordAudit(UUID tenantId, String action, boolean successful) {
        UUID actor = auditActor(tenantId);
        jdbc.update("""
                INSERT INTO audit_logs
                    (id, user_id, action, module, entity, entity_id, status, audit_timestamp)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """, UUID.randomUUID(), actor, action, "TENANT_MANAGEMENT", "TenantDatabase",
                tenantId.toString(), successful ? "SUCCESS" : "FAILURE", Date.valueOf(java.time.LocalDate.now()));
    }

    private UUID auditActor(UUID tenantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            try {
                return UUID.fromString(authentication.getName());
            } catch (IllegalArgumentException exception) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Authenticated audit user must be a UUID");
            }
        }
        List<UUID> users = jdbc.queryForList("""
                SELECT id FROM users
                WHERE tenant_id = ? AND status = 'ACTIVE' AND is_deleted = false
                ORDER BY created_at, id
                LIMIT 1
                """, UUID.class, tenantId);
        if (users == null || users.isEmpty())
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Audit requires an active user for this tenant");
        return users.getFirst();
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
