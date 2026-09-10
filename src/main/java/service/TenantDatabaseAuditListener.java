package com.tenant_management.service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/** Writes to the existing shared table in the caller's transaction. */
@Component
public class TenantDatabaseAuditListener {
    private final JdbcTemplate jdbc;

    public TenantDatabaseAuditListener(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void record(TenantDatabaseAuditEvent event) {
        UUID actor = actorId(event.tenantId());
        jdbc.update("""
                INSERT INTO audit_logs
                    (id, user_id, action, module, entity, entity_id, status, audit_timestamp)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """, UUID.randomUUID(), actor, event.action(), "TENANT_MANAGEMENT",
                "TenantDatabase", event.tenantId().toString(),
                event.successful() ? "SUCCESS" : "FAILURE", Date.valueOf(event.occurredAt().toLocalDate()));
    }

    private UUID actorId(UUID tenantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        if (authenticated) {
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
}
