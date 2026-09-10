package com.tenant_management.service;

import java.sql.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
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
    private final String systemUserId;

    public TenantDatabaseAuditListener(JdbcTemplate jdbc,
            @Value("${tenant.database.audit.system-user-id:}") String systemUserId) {
        this.jdbc = jdbc;
        this.systemUserId = systemUserId;
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void record(TenantDatabaseAuditEvent event) {
        UUID actor = actorId();
        jdbc.update("""
                INSERT INTO audit_logs
                    (id, user_id, action, module, entity, entity_id, status, audit_timestamp)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """, UUID.randomUUID(), actor, event.action(), "TENANT_MANAGEMENT",
                "TenantDatabase", event.tenantId().toString(),
                event.successful() ? "SUCCESS" : "FAILURE", Date.valueOf(event.occurredAt().toLocalDate()));
    }

    private UUID actorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        String value = authenticated ? authentication.getName() : systemUserId;
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Audit requires an authenticated user UUID or a configured system user UUID");
        }
    }
}
