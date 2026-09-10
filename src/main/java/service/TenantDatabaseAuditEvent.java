package com.tenant_management.service;

import java.time.LocalDateTime;
import java.util.UUID;

/** Integration event for the shared audit_logs adapter. Contains no connection credentials. */
public record TenantDatabaseAuditEvent(UUID tenantId, String action, boolean successful,
                                       LocalDateTime occurredAt) {}
