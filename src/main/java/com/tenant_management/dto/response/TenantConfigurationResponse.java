package com.tenant_management.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class TenantConfigurationResponse {

    private UUID id;
    private UUID tenantId;
    private String country;
    private String timeZone;
    private String language;
    private String currency;
    private String dateFormat;
    private String passwordPolicy;
    private Integer sessionTimeoutMinutes;
    private Boolean mfaEnabled;
    private BigDecimal storageLimitGb;
    private Boolean emailNotificationsEnabled;
    private Boolean smsNotificationsEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}