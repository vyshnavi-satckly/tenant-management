package com.tenant_management.service;

import com.tenant_management.controller.TenantDatabaseController;
import com.tenant_management.dto.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TenantDatabaseControllerTests {
    TenantDatabaseService service;
    MockMvc mvc;
    UUID id = UUID.randomUUID();
    String path = "/api/tenants/" + id + "/database";

    @BeforeEach void setup() {
        service = mock(TenantDatabaseService.class);
        mvc = MockMvcBuilders.standaloneSetup(new TenantDatabaseController(service)).build();
    }

    @Test void exposesReadAndUpdateEndpoints() throws Exception {
        when(service.getDatabase(id)).thenReturn(new TenantDatabaseResponse());
        when(service.updateDatabase(eq(id), any())).thenReturn(new TenantDatabaseResponse());
        mvc.perform(get(path)).andExpect(status().isOk());
        mvc.perform(put(path).contentType("application/json").content("""
                {"databaseType":"POSTGRESQL","serverName":"localhost:5432",
                 "allocatedStorageGb":20,"autoBackupEnabled":true,"maintenanceWindow":"Sunday"}
                """)).andExpect(status().isOk());
        verify(service).updateDatabase(eq(id), argThat(r -> r.getAllocatedStorageGb().intValue() == 20));
    }

    @Test void exposesConnectionAndHealthEndpoints() throws Exception {
        var connection = new DatabaseConnectionResult(false, "DISCONNECTED", "Checked", LocalDateTime.now());
        when(service.testConnection(eq(id), any())).thenReturn(connection);
        when(service.getHealth(id)).thenReturn(new TenantDatabaseHealthResponse(id, "tenant_test", connection,
                null, null, null, null, null));
        mvc.perform(post(path + "/test-connection").contentType("application/json")
                .content("{\"databaseType\":\"POSTGRESQL\",\"serverName\":\"localhost:5432\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.connected").value(false));
        mvc.perform(get(path + "/health")).andExpect(status().isOk())
                .andExpect(jsonPath("$.connection.status").value("DISCONNECTED"));
    }

    @Test void returnsNotFoundAndRejectsMalformedTenantId() throws Exception {
        when(service.getDatabase(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Tenant not found"));
        mvc.perform(get(path)).andExpect(status().isNotFound());
        mvc.perform(get("/api/tenants/not-a-uuid/database")).andExpect(status().isBadRequest());
    }
}
