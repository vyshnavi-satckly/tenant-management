package com.tenant_management.controller;

import com.tenant_management.dto.request.TenantConfigurationRequestDto;
import com.tenant_management.dto.response.TenantConfigurationResponseDto;
import com.tenant_management.service.TenantConfigurationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TenantConfigurationController.class)
@AutoConfigureMockMvc(addFilters = false)
class TenantConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TenantConfigurationService tenantConfigurationService;


    @Test
    void getConfiguration_shouldReturnConfiguration() throws Exception {

        UUID tenantId = UUID.randomUUID();

        TenantConfigurationResponseDto response =
                TenantConfigurationResponseDto.builder()
                        .id(UUID.randomUUID())
                        .tenantId(tenantId)
                        .country("India")
                        .timeZone("Asia/Kolkata")
                        .language("English")
                        .currency("INR")
                        .dateFormat("dd-MM-yyyy")
                        .passwordPolicy("Strong")
                        .sessionTimeoutMinutes(30)
                        .mfaEnabled(true)
                        .storageLimitGb(new BigDecimal("10"))
                        .emailNotificationsEnabled(true)
                        .smsNotificationsEnabled(false)
                        .build();

        when(tenantConfigurationService.getConfiguration(tenantId))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/tenants/{tenantId}/configuration", tenantId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantId").value(tenantId.toString()))
                .andExpect(jsonPath("$.country").value("India"))
                .andExpect(jsonPath("$.timeZone").value("Asia/Kolkata"))
                .andExpect(jsonPath("$.currency").value("INR"));

        verify(tenantConfigurationService)
                .getConfiguration(tenantId);
    }


    @Test
    void updateConfiguration_shouldReturnUpdatedConfiguration()
            throws Exception {

        UUID tenantId = UUID.randomUUID();

        TenantConfigurationResponseDto response =
                TenantConfigurationResponseDto.builder()
                        .id(UUID.randomUUID())
                        .tenantId(tenantId)
                        .country("India")
                        .timeZone("Asia/Kolkata")
                        .language("English")
                        .currency("INR")
                        .dateFormat("dd-MM-yyyy")
                        .passwordPolicy("Strong")
                        .sessionTimeoutMinutes(60)
                        .mfaEnabled(true)
                        .storageLimitGb(new BigDecimal("20"))
                        .emailNotificationsEnabled(true)
                        .smsNotificationsEnabled(false)
                        .build();

        when(tenantConfigurationService.updateConfiguration(
                eq(tenantId),
                any(TenantConfigurationRequestDto.class),
                nullable(UUID.class)
        )).thenReturn(response);

        String requestJson = """
                {
                  "country": "India",
                  "timeZone": "Asia/Kolkata",
                  "language": "English",
                  "currency": "INR",
                  "dateFormat": "dd-MM-yyyy",
                  "passwordPolicy": "Strong",
                  "sessionTimeoutMinutes": 60,
                  "mfaEnabled": true,
                  "storageLimitGb": 20,
                  "emailNotificationsEnabled": true,
                  "smsNotificationsEnabled": false
                }
                """;

        mockMvc.perform(
                        put("/api/tenants/{tenantId}/configuration", tenantId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantId")
                        .value(tenantId.toString()))
                .andExpect(jsonPath("$.country")
                        .value("India"))
                .andExpect(jsonPath("$.sessionTimeoutMinutes")
                        .value(60))
                .andExpect(jsonPath("$.storageLimitGb")
                        .value(20));

        verify(tenantConfigurationService)
                .updateConfiguration(
                        eq(tenantId),
                        any(TenantConfigurationRequestDto.class),
                        nullable(UUID.class)
                );
    }


    @Test
    void updateConfiguration_shouldReturnBadRequest_whenValidationFails()
            throws Exception {

        UUID tenantId = UUID.randomUUID();

        String invalidRequestJson = """
                {
                  "country": "",
                  "timeZone": "",
                  "language": "",
                  "currency": "",
                  "dateFormat": "dd-MM-yyyy",
                  "passwordPolicy": "",
                  "sessionTimeoutMinutes": 300,
                  "mfaEnabled": null,
                  "storageLimitGb": 0,
                  "emailNotificationsEnabled": null,
                  "smsNotificationsEnabled": null
                }
                """;

        mockMvc.perform(
                        put("/api/tenants/{tenantId}/configuration", tenantId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidRequestJson)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tenantConfigurationService);
    }
}