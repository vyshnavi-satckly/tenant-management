package com.tenant_management.mapper;

import com.tenant_management.dto.request.TenantConfigurationRequestDto;
import com.tenant_management.dto.response.TenantConfigurationResponseDto;
import com.tenant_management.entity.TenantConfiguration;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TenantConfigurationMapperTest {

    @Test
    void toEntity_shouldMapAllFieldsCorrectly() {
        TenantConfigurationRequestDto request = new TenantConfigurationRequestDto();
        request.setCountry("India");
        request.setTimeZone("Asia/Kolkata");
        request.setLanguage("en");
        request.setCurrency("INR");
        request.setDateFormat("dd-MM-yyyy");
        request.setPasswordPolicy("STRONG");
        request.setSessionTimeoutMinutes(30);
        request.setMfaEnabled(true);
        request.setStorageLimitGb(BigDecimal.valueOf(50));
        request.setEmailNotificationsEnabled(true);
        request.setSmsNotificationsEnabled(false);

        UUID tenantId = UUID.randomUUID();
        TenantConfiguration entity = TenantConfigurationMapper.toEntity(request, tenantId);

        assertThat(entity.getTenantId()).isEqualTo(tenantId);
        assertThat(entity.getCountry()).isEqualTo("India");
        assertThat(entity.getTimeZone()).isEqualTo("Asia/Kolkata");
        assertThat(entity.getLanguage()).isEqualTo("en");
        assertThat(entity.getCurrency()).isEqualTo("INR");
        assertThat(entity.getDateFormat()).isEqualTo("dd-MM-yyyy");
        assertThat(entity.getPasswordPolicy()).isEqualTo("STRONG");
        assertThat(entity.getSessionTimeoutMinutes()).isEqualTo(30);
        assertThat(entity.getMfaEnabled()).isTrue();
        assertThat(entity.getStorageLimitGb()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(entity.getEmailNotificationsEnabled()).isTrue();
        assertThat(entity.getSmsNotificationsEnabled()).isFalse();
    }

    @Test
    void toResponse_shouldMapAllFieldsCorrectly() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        TenantConfiguration entity = TenantConfiguration.builder()
                .id(id)
                .tenantId(tenantId)
                .country("India")
                .timeZone("Asia/Kolkata")
                .language("en")
                .currency("INR")
                .passwordPolicy("STRONG")
                .sessionTimeoutMinutes(30)
                .mfaEnabled(true)
                .storageLimitGb(BigDecimal.valueOf(50))
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        TenantConfigurationResponseDto response = TenantConfigurationMapper.toResponse(entity);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTenantId()).isEqualTo(tenantId);
        assertThat(response.getCountry()).isEqualTo("India");
        assertThat(response.getSessionTimeoutMinutes()).isEqualTo(30);
        assertThat(response.getStorageLimitGb()).isEqualByComparingTo(BigDecimal.valueOf(50));
    }
}