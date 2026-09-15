package com.tenant_management.dto;

import java.math.BigDecimal;
import java.util.UUID;

// Storage and utilization are the last recorded metrics, not live server telemetry.
public record TenantDatabaseHealthResponse(UUID tenantId, String databaseName,
        DatabaseConnectionResult connection, BigDecimal allocatedStorageGb,
        BigDecimal usedStorageGb, BigDecimal availableStorageGb,
        BigDecimal cpuUsage, BigDecimal memoryUsage) {}
