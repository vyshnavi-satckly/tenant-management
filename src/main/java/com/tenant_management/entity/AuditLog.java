package com.tenant_management.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID userId; // can be null

    @Column(name = "action")
    private String action;

    @Column(name = "module")
    private String module;

    @Column(name = "entity") // don't use quotes here, Hibernate will handle
    private String entity;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "status")
    private String status;

    @Column(name = "audit_timestamp")
    private LocalDate auditTimestamp;

    // Getters & Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getEntity() { return entity; }
    public void setEntity(String entity) { this.entity = entity; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getAuditTimestamp() { return auditTimestamp; }
    public void setAuditTimestamp(LocalDate auditTimestamp) { this.auditTimestamp = auditTimestamp; }
}