package com.tenant_management.service;

import com.tenant_management.dto.request.TenantConfigurationRequestDto;
import com.tenant_management.entity.TenantConfiguration;
import com.tenant_management.repository.TenantConfigurationRepository;
import com.tenant_management.service.impl.TenantConfigurationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantConfigurationServiceImplTest {

    @Mock
    private TenantConfigurationRepository tenantConfigurationRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TenantConfigurationServiceImpl tenantConfigurationService;

    @Test
    void getConfiguration_shouldReturnConfiguration() {

        UUID tenantId = UUID.randomUUID();

        TenantConfiguration configuration =
                new TenantConfiguration();

        configuration.setId(UUID.randomUUID());
        configuration.setTenantId(tenantId);
        configuration.setCountry("India");
        configuration.setTimeZone("Asia/Kolkata");
        configuration.setLanguage("English");
        configuration.setCurrency("INR");

        when(tenantConfigurationRepository.findByTenantId(tenantId))
                .thenReturn(Optional.of(configuration));

        var response =
                tenantConfigurationService.getConfiguration(tenantId);

        assertNotNull(response);
        assertEquals(tenantId, response.getTenantId());
        assertEquals("India", response.getCountry());

        verify(tenantConfigurationRepository)
                .findByTenantId(tenantId);
    }

    @Test
    void getConfiguration_shouldThrowException_whenTenantIdIsNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> tenantConfigurationService.getConfiguration(null)
        );

        verifyNoInteractions(tenantConfigurationRepository);
    }

    @Test
    void getConfiguration_shouldThrowException_whenConfigurationNotFound() {

        UUID tenantId = UUID.randomUUID();

        when(tenantConfigurationRepository.findByTenantId(tenantId))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> tenantConfigurationService.getConfiguration(tenantId)
        );

        verify(tenantConfigurationRepository)
                .findByTenantId(tenantId);
    }

    @Test
    void updateConfiguration_shouldCreateNewConfiguration_whenNotFound() {

        UUID tenantId = UUID.randomUUID();
        UUID updatedBy = UUID.randomUUID();

        TenantConfigurationRequestDto request =
                new TenantConfigurationRequestDto();

        request.setCountry("India");
        request.setTimeZone("Asia/Kolkata");
        request.setLanguage("English");
        request.setCurrency("INR");
        request.setDateFormat("dd-MM-yyyy");
        request.setPasswordPolicy("Strong");
        request.setSessionTimeoutMinutes(30);
        request.setMfaEnabled(true);
        request.setStorageLimitGb(new BigDecimal("10"));
        request.setEmailNotificationsEnabled(true);
        request.setSmsNotificationsEnabled(false);

        when(tenantConfigurationRepository.findByTenantId(tenantId))
                .thenReturn(Optional.empty());

        TenantConfiguration savedConfiguration =
                new TenantConfiguration();

        savedConfiguration.setId(UUID.randomUUID());
        savedConfiguration.setTenantId(tenantId);
        savedConfiguration.setCountry("India");
        savedConfiguration.setTimeZone("Asia/Kolkata");
        savedConfiguration.setLanguage("English");
        savedConfiguration.setCurrency("INR");

        when(tenantConfigurationRepository.save(
                any(TenantConfiguration.class)))
                .thenReturn(savedConfiguration);

        var response =
                tenantConfigurationService.updateConfiguration(
                        tenantId,
                        request,
                        updatedBy
                );

        assertNotNull(response);
        assertEquals(tenantId, response.getTenantId());
        assertEquals("India", response.getCountry());

        verify(tenantConfigurationRepository)
                .findByTenantId(tenantId);

        verify(tenantConfigurationRepository)
                .save(any(TenantConfiguration.class));
    }

    @Test
    void updateConfiguration_shouldUpdateExistingConfiguration() {

        UUID tenantId = UUID.randomUUID();
        UUID updatedBy = UUID.randomUUID();

        TenantConfigurationRequestDto request =
                new TenantConfigurationRequestDto();

        request.setCountry("India");
        request.setTimeZone("Asia/Kolkata");
        request.setLanguage("English");
        request.setCurrency("INR");
        request.setDateFormat("dd-MM-yyyy");
        request.setPasswordPolicy("Strong");
        request.setSessionTimeoutMinutes(60);
        request.setMfaEnabled(true);
        request.setStorageLimitGb(new BigDecimal("20"));
        request.setEmailNotificationsEnabled(true);
        request.setSmsNotificationsEnabled(false);

        TenantConfiguration existingConfiguration =
                new TenantConfiguration();

        existingConfiguration.setId(UUID.randomUUID());
        existingConfiguration.setTenantId(tenantId);
        existingConfiguration.setCountry("USA");
        existingConfiguration.setTimeZone("America/New_York");
        existingConfiguration.setLanguage("English");
        existingConfiguration.setCurrency("USD");

        when(tenantConfigurationRepository.findByTenantId(tenantId))
                .thenReturn(Optional.of(existingConfiguration));

        when(tenantConfigurationRepository.save(existingConfiguration))
                .thenReturn(existingConfiguration);

        var response =
                tenantConfigurationService.updateConfiguration(
                        tenantId,
                        request,
                        updatedBy
                );

        assertNotNull(response);
        assertEquals(tenantId, response.getTenantId());
        assertEquals("India", response.getCountry());
        assertEquals("Asia/Kolkata", response.getTimeZone());
        assertEquals("English", response.getLanguage());
        assertEquals("INR", response.getCurrency());

        verify(tenantConfigurationRepository)
                .findByTenantId(tenantId);

        verify(tenantConfigurationRepository)
                .save(existingConfiguration);
    }
}