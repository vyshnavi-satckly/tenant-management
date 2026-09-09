package com.tenant_management.mapper;

import com.tenant_management.dto.request.TenantConfigurationRequestDto;
import com.tenant_management.dto.response.TenantConfigurationResponseDto;
import com.tenant_management.entity.TenantConfiguration;

import java.util.UUID;

public class TenantConfigurationMapper {

    private TenantConfigurationMapper() {
    }

    public static TenantConfiguration toEntity(TenantConfigurationRequestDto request, UUID tenantId) {
        return TenantConfiguration.builder()
                .tenantId(tenantId)
                .country(request.getCountry())
                .timeZone(request.getTimeZone())
                .language(request.getLanguage())
                .currency(request.getCurrency())
                .dateFormat(request.getDateFormat())
                .passwordPolicy(request.getPasswordPolicy())
                .sessionTimeoutMinutes(request.getSessionTimeoutMinutes())
                .mfaEnabled(request.getMfaEnabled())
                .storageLimitGb(request.getStorageLimitGb())
                .emailNotificationsEnabled(request.getEmailNotificationsEnabled())
                .smsNotificationsEnabled(request.getSmsNotificationsEnabled())
                .build();
    }

    public static void updateEntity(TenantConfiguration entity, TenantConfigurationRequestDto request) {
        entity.setCountry(request.getCountry());
        entity.setTimeZone(request.getTimeZone());
        entity.setLanguage(request.getLanguage());
        entity.setCurrency(request.getCurrency());
        entity.setDateFormat(request.getDateFormat());
        entity.setPasswordPolicy(request.getPasswordPolicy());
        entity.setSessionTimeoutMinutes(request.getSessionTimeoutMinutes());
        entity.setMfaEnabled(request.getMfaEnabled());
        entity.setStorageLimitGb(request.getStorageLimitGb());
        entity.setEmailNotificationsEnabled(request.getEmailNotificationsEnabled());
        entity.setSmsNotificationsEnabled(request.getSmsNotificationsEnabled());
    }

    public static TenantConfigurationResponseDto toResponse(TenantConfiguration entity) {
        return TenantConfigurationResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .country(entity.getCountry())
                .timeZone(entity.getTimeZone())
                .language(entity.getLanguage())
                .currency(entity.getCurrency())
                .dateFormat(entity.getDateFormat())
                .passwordPolicy(entity.getPasswordPolicy())
                .sessionTimeoutMinutes(entity.getSessionTimeoutMinutes())
                .mfaEnabled(entity.getMfaEnabled())
                .storageLimitGb(entity.getStorageLimitGb())
                .emailNotificationsEnabled(entity.getEmailNotificationsEnabled())
                .smsNotificationsEnabled(entity.getSmsNotificationsEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}