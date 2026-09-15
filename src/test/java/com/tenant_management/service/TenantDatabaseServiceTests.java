package com.tenant_management.service;

import com.tenant_management.dto.request.TenantDatabaseRequest;
import com.tenant_management.entity.Tenant;
import com.tenant_management.entity.TenantDatabase;
import com.tenant_management.repository.TenantDatabaseRepository;
import com.tenant_management.repository.TenantRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TenantDatabaseServiceTests {
    private TenantDatabaseRepository databases;
    private TenantDatabaseService service;
    private UUID tenantId;
    private TenantDatabaseRequest request;

    @BeforeEach
    void setup() throws Exception {
        databases = mock(TenantDatabaseRepository.class);
        TenantRepository tenants = mock(TenantRepository.class);
        DataSource dataSource = mock(DataSource.class);
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        Connection connection = mock(Connection.class);
        tenantId = UUID.randomUUID();
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        TenantDatabase database = new TenantDatabase();
        database.setTenant(tenant);
        database.setDatabaseName("cloud_platform");
        database.setUsedStorageGb(BigDecimal.TEN);

        when(tenants.findById(tenantId)).thenReturn(Optional.of(tenant));
        when(databases.findByTenant_Id(tenantId)).thenReturn(Optional.of(database));
        when(databases.save(any())).thenAnswer(call -> call.getArgument(0));
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(5)).thenReturn(true);
        when(jdbc.queryForList(anyString(), eq(UUID.class), eq(tenantId)))
                .thenReturn(List.of(UUID.randomUUID()));
        service = new TenantDatabaseService(databases, tenants, dataSource, jdbc,
                "jdbc:postgresql://localhost:5432/cloud_platform");

        request = new TenantDatabaseRequest();
        request.setDatabaseType("POSTGRESQL");
        request.setServerName("localhost:5432");
        request.setAllocatedStorageGb(new BigDecimal("20"));
        request.setAutoBackupEnabled(true);
        request.setMaintenanceWindow("Sunday 02:00-03:00 UTC");
    }

    @Test
    void putSavesVerifiedSettingsAndCalculatesAvailableStorage() {
        var response = service.updateDatabase(tenantId, request);
        assertEquals(new BigDecimal("10"), response.getAvailableStorageGb());
        assertEquals("cloud_platform", response.getDatabaseName());
    }

    @Test
    void putRejectsStorageBelowCurrentUsage() {
        request.setAllocatedStorageGb(new BigDecimal("9"));
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.updateDatabase(tenantId, request)).getStatusCode().value());
        verify(databases, never()).save(any());
    }

    @Test
    void connectionTestDoesNotSaveConfiguration() {
        assertTrue(service.testConnection(tenantId, request).connected());
        verify(databases, never()).save(any());
    }
}
