package com.tenant_management.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TenantConfigurationNotFoundException extends RuntimeException {

    public TenantConfigurationNotFoundException(String message) {
        super(message);
    }
}