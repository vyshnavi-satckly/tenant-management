package com.tenant_management.service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TenantDatabaseAuditListenerTests {
    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final UUID actor = UUID.randomUUID();
    private final TenantDatabaseAuditEvent event = new TenantDatabaseAuditEvent(
            UUID.randomUUID(), "DATABASE_UPDATED", true, LocalDateTime.now());

    @AfterEach void cleanup() { SecurityContextHolder.clearContext(); }

    @Test void persistsToSharedTableUsingConfiguredSystemActor() {
        new TenantDatabaseAuditListener(jdbc, actor.toString()).record(event);
        verify(jdbc).update(contains("INSERT INTO audit_logs"), any(UUID.class), eq(actor),
                eq("DATABASE_UPDATED"), eq("TENANT_MANAGEMENT"), eq("TenantDatabase"),
                eq(event.tenantId().toString()), eq("SUCCESS"), eq(Date.valueOf(event.occurredAt().toLocalDate())));
    }

    @Test void authenticatedActorTakesPrecedenceAndFailedCheckIsRecorded() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(actor.toString(), "", java.util.List.of()));
        new TenantDatabaseAuditListener(jdbc, UUID.randomUUID().toString()).record(
                new TenantDatabaseAuditEvent(event.tenantId(), "DATABASE_CONNECTION_TESTED", false, event.occurredAt()));
        verify(jdbc).update(anyString(), any(UUID.class), eq(actor), eq("DATABASE_CONNECTION_TESTED"),
                eq("TENANT_MANAGEMENT"), eq("TenantDatabase"), eq(event.tenantId().toString()),
                eq("FAILURE"), any(Date.class));
    }

    @Test void missingActorDoesNotSilentlyDropAudit() {
        assertEquals(503, assertThrows(ResponseStatusException.class,
                () -> new TenantDatabaseAuditListener(jdbc, "").record(event)).getStatusCode().value());
        verifyNoInteractions(jdbc);
    }

    @Test void persistenceFailurePropagatesToRollbackConfigurationSave() {
        doThrow(new org.springframework.dao.DataIntegrityViolationException("Unknown user"))
                .when(jdbc).update(anyString(), any(Object[].class));
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> new TenantDatabaseAuditListener(jdbc, actor.toString()).record(event));
    }
}
