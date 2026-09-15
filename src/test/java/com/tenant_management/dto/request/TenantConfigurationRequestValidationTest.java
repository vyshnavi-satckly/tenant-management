package com.tenant_management.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TenantConfigurationRequestValidationTest {

    private final Validator validator;

    TenantConfigurationRequestValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    private TenantConfigurationRequestDto validRequest() {
        TenantConfigurationRequestDto request = new TenantConfigurationRequestDto();
        request.setCountry("India");
        request.setTimeZone("Asia/Kolkata");
        request.setLanguage("en");
        request.setCurrency("INR");
        request.setPasswordPolicy("STRONG");
        request.setSessionTimeoutMinutes(30);
        request.setMfaEnabled(true);
        request.setStorageLimitGb(BigDecimal.valueOf(50));
        request.setEmailNotificationsEnabled(true);
        request.setSmsNotificationsEnabled(false);
        return request;
    }

    @Test
    void validRequest_shouldHaveNoViolations() {
        Set<ConstraintViolation<TenantConfigurationRequestDto>> violations = validator.validate(validRequest());
        assertThat(violations).isEmpty();
    }

    @Test
    void sessionTimeoutBelowMinimum_shouldFailValidation() {
        TenantConfigurationRequestDto request = validRequest();
        request.setSessionTimeoutMinutes(3); // below minimum of 5

        Set<ConstraintViolation<TenantConfigurationRequestDto>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void sessionTimeoutAboveMaximum_shouldFailValidation() {
        TenantConfigurationRequestDto request = validRequest();
        request.setSessionTimeoutMinutes(300); // above maximum of 240

        Set<ConstraintViolation<TenantConfigurationRequestDto>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void storageLimitZero_shouldFailValidation() {
        TenantConfigurationRequestDto request = validRequest();
        request.setStorageLimitGb(BigDecimal.ZERO);

        Set<ConstraintViolation<TenantConfigurationRequestDto>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void blankCountry_shouldFailValidation() {
        TenantConfigurationRequestDto request = validRequest();
        request.setCountry("");

        Set<ConstraintViolation<TenantConfigurationRequestDto>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }
}