package com.tenant_management.exception;

public class TenantNotFoundException extends RuntimeException {

    public TenantNotFoundException(String tenantId) {
        super("Tenant not found: " + tenantId);
    }
}