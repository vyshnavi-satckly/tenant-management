package com.tenant_management.service;

import com.tenant_management.dto.*;
import com.tenant_management.entity.*;
import com.tenant_management.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TenantDatabaseServiceTests {
    TenantDatabaseRepository databases;
    TenantRepository tenants;
    DatabaseConnectionVerifier verifier;
    ApplicationEventPublisher events;
    TenantDatabaseServiceImpl service;
    UUID id;
    Tenant tenant;
    TenantDatabase database;
    TenantDatabaseRequest request;

    @BeforeEach
    void setup() {
        databases = mock(TenantDatabaseRepository.class);
        tenants = mock(TenantRepository.class);
        verifier = mock(DatabaseConnectionVerifier.class);
        events = mock(ApplicationEventPublisher.class);
        service = new TenantDatabaseServiceImpl(databases, tenants, verifier, events);
        id = UUID.randomUUID();
        tenant = new Tenant(); tenant.setId(id);
        database = new TenantDatabase(); database.setTenant(tenant);
        database.setDatabaseName("cloud_platform");
        database.setUsedStorageGb(new BigDecimal("10"));
        database.setAllocatedStorageGb(new BigDecimal("20"));
        database.setDatabaseType("POSTGRESQL"); database.setServerName("localhost:5432");
        request = new TenantDatabaseRequest();
        request.setDatabaseType("POSTGRESQL"); request.setServerName("localhost:5432");
        request.setAllocatedStorageGb(new BigDecimal("15"));
        request.setMaintenanceWindow("Sunday 02:00-03:00 UTC"); request.setAutoBackupEnabled(true);
        when(tenants.findById(id)).thenReturn(Optional.of(tenant));
        when(databases.findByTenant_Id(id)).thenReturn(Optional.of(database));
        when(databases.save(any())).thenAnswer(call -> call.getArgument(0));
        when(verifier.verify(anyString(), anyString(), anyString())).thenReturn(result(true));
    }

    DatabaseConnectionResult result(boolean connected) {
        return new DatabaseConnectionResult(connected, connected ? "CONNECTED" : "DISCONNECTED", "Checked", LocalDateTime.now());
    }

    @Test void rejectsStorageBelowUsageWithoutConnectingOrSaving() {
        request.setAllocatedStorageGb(new BigDecimal("9"));
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.updateDatabase(id, request)).getStatusCode().value());
        verifyNoInteractions(verifier); verify(databases, never()).save(any());
    }

    @Test void failedConnectionDoesNotMutateSettingsOrEmitSuccess() {
        when(verifier.verify(anyString(), anyString(), anyString())).thenReturn(result(false));
        assertThrows(ResponseStatusException.class, () -> service.updateDatabase(id, request));
        assertEquals(new BigDecimal("20"), database.getAllocatedStorageGb());
        verify(databases, never()).save(any()); verifyNoInteractions(events);
    }

    @Test void savesVerifiedSettingsAndUsesSharedDatabaseName() {
        TenantDatabaseResponse response = service.updateDatabase(id, request);
        assertEquals("cloud_platform", response.getDatabaseName());
        assertEquals(new BigDecimal("5"), response.getAvailableStorageGb());
        verify(verifier).verify("POSTGRESQL", "localhost:5432", "cloud_platform");
        verify(events).publishEvent(any(TenantDatabaseAuditEvent.class));
    }

    @Test void permitsAllocationEqualToUsage() {
        request.setAllocatedStorageGb(BigDecimal.TEN);
        assertEquals(BigDecimal.ZERO, service.updateDatabase(id, request).getAvailableStorageGb());
    }

    @Test void readNormalizesLegacyDatabaseNameToSharedDatabase() {
        database.setDatabaseName("legacy_tenant_database");
        TenantDatabaseResponse response = service.getDatabase(id);
        assertEquals("cloud_platform", response.getDatabaseName());
        verify(databases).save(database);
    }

    @Test void putInitializesMissingConfigurationWithSharedDatabaseName() {
        when(databases.findByTenant_Id(id)).thenReturn(Optional.empty());
        TenantDatabaseResponse response = service.updateDatabase(id, request);
        assertEquals("cloud_platform", response.getDatabaseName());
        assertEquals(BigDecimal.ZERO, response.getUsedStorageGb());
    }

    @Test void missingAndDeletedTenantsReturnNotFound() {
        when(tenants.findById(id)).thenReturn(Optional.empty());
        assertEquals(404, assertThrows(ResponseStatusException.class, () -> service.getDatabase(id)).getStatusCode().value());
        when(tenants.findById(id)).thenReturn(Optional.of(tenant)); tenant.setIsDeleted(true);
        assertEquals(404, assertThrows(ResponseStatusException.class, () -> service.getHealth(id)).getStatusCode().value());
        verifyNoInteractions(verifier);
    }

    @Test void missingMaintenanceWindowIsRejected() {
        request.setMaintenanceWindow("  ");
        assertThrows(ResponseStatusException.class, () -> service.updateDatabase(id, request));
        verify(databases, never()).save(any());
    }

    @Test void duplicateCreateIsRejected() {
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.createDatabase(id, request)).getStatusCode().value());
        verify(databases, never()).save(any());
    }

    @Test void createValidatesMaintenanceBeforeConnecting() {
        when(databases.findByTenant_Id(id)).thenReturn(Optional.empty());
        request.setMaintenanceWindow(null);
        assertThrows(ResponseStatusException.class, () -> service.createDatabase(id, request));
        verifyNoInteractions(verifier); verify(databases, never()).save(any());
    }

    @Test void createRejectsFailedVerification() {
        when(databases.findByTenant_Id(id)).thenReturn(Optional.empty());
        when(verifier.verify(anyString(), anyString(), anyString())).thenReturn(result(false));
        assertThrows(ResponseStatusException.class, () -> service.createDatabase(id, request));
        verify(databases, never()).save(any()); verifyNoInteractions(events);
    }

    @Test void rejectsMissingZeroAndNegativeStorage() {
        for (BigDecimal storage : new BigDecimal[] {null, BigDecimal.ZERO, BigDecimal.ONE.negate()}) {
            request.setAllocatedStorageGb(storage);
            assertThrows(ResponseStatusException.class, () -> service.updateDatabase(id, request));
        }
        verifyNoInteractions(verifier); verify(databases, never()).save(any());
    }

    @Test void failedConnectionTestIsAuditedWithoutSaving() {
        when(verifier.verify(anyString(), anyString(), anyString())).thenReturn(result(false));
        assertFalse(service.testConnection(id, request).connected());
        verify(events).publishEvent(argThat((Object event) -> event instanceof TenantDatabaseAuditEvent audit
                && !audit.successful() && audit.action().equals("DATABASE_CONNECTION_TESTED")));
        verify(databases, never()).save(any());
    }

    @Test void connectionTestUsesCandidateSettingsWithoutSaving() {
        request.setServerName("db.example:5432");
        assertTrue(service.testConnection(id, request).connected());
        verify(verifier).verify("POSTGRESQL", "db.example:5432", "cloud_platform");
        verify(databases, never()).save(any());
    }

    @Test void healthChecksLiveConnectionWithoutInventingMetrics() {
        when(verifier.verify(anyString(), anyString(), anyString())).thenReturn(result(false));
        TenantDatabaseHealthResponse health = service.getHealth(id);
        assertFalse(health.connection().connected());
        assertNull(health.cpuUsage()); assertNull(health.memoryUsage());
        verify(databases, never()).save(any());
    }
}
