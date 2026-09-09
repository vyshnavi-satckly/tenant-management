package com.tenant_management.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TenantConfigurationRequestDto {

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Time zone is required")
    private String timeZone;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Currency is required")
    private String currency;

    private String dateFormat;

    @NotBlank(message = "Password policy is required")
    private String passwordPolicy;

    @NotNull(message = "Session timeout is required")
    @Min(value = 5, message = "Session timeout must be at least 5 minutes")
    @Max(value = 240, message = "Session timeout must not exceed 240 minutes")
    private Integer sessionTimeoutMinutes;

    @NotNull(message = "MFA enabled flag is required")
    private Boolean mfaEnabled;

    @NotNull(message = "Storage limit is required")
    @DecimalMin(value = "0.01", message = "Storage limit must be greater than 0 GB")
    private BigDecimal storageLimitGb;

    @NotNull(message = "Email notification flag is required")
    private Boolean emailNotificationsEnabled;

    @NotNull(message = "SMS notification flag is required")
    private Boolean smsNotificationsEnabled;
}