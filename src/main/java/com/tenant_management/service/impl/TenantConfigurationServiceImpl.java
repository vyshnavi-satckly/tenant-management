package com.tenant_management.service.impl;

import com.tenant_management.dto.request.TenantConfigurationRequestDto;
import com.tenant_management.dto.response.TenantConfigurationResponseDto;
import com.tenant_management.entity.TenantConfiguration;
import com.tenant_management.mapper.TenantConfigurationMapper;
import com.tenant_management.repository.TenantConfigurationRepository;
import com.tenant_management.service.TenantConfigurationService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TenantConfigurationServiceImpl
        implements TenantConfigurationService {

    private final TenantConfigurationRepository tenantConfigurationRepository;
    private final JdbcTemplate jdbcTemplate;

    public TenantConfigurationServiceImpl(
            TenantConfigurationRepository tenantConfigurationRepository,
            JdbcTemplate jdbcTemplate) {

        this.tenantConfigurationRepository =
                tenantConfigurationRepository;

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TenantConfigurationResponseDto getConfiguration(UUID tenantId) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID is required");
        }

        TenantConfiguration configuration =
                tenantConfigurationRepository.findByTenantId(tenantId)
                        .orElseThrow(() ->
                                new TenantConfigurationNotFoundException(
                                        "Tenant configuration not found for tenantId: "
                                                + tenantId
                                )
                        );

        return TenantConfigurationMapper.toResponse(configuration);
    }

    @Override
    public TenantConfigurationResponseDto updateConfiguration(
            UUID tenantId,
            TenantConfigurationRequestDto request,
            UUID updatedBy) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID is required");
        }

        if (request == null) {
            throw new IllegalArgumentException(
                    "Configuration request is required"
            );
        }

        TenantConfiguration configuration =
                tenantConfigurationRepository.findByTenantId(tenantId)
                        .orElseGet(() ->
                                TenantConfigurationMapper.toEntity(
                                        request,
                                        tenantId
                                )
                        );

        if (configuration.getId() != null) {

            TenantConfigurationMapper.updateEntity(
                    configuration,
                    request
            );
        }

        TenantConfiguration savedConfiguration =
                tenantConfigurationRepository.save(configuration);

        logAudit(
                tenantId,
                "UPDATE",
                "TENANT_MANAGEMENT",
                "TENANT_CONFIGURATION",
                updatedBy
        );

        return TenantConfigurationMapper.toResponse(savedConfiguration);
    }

    private void logAudit(
            UUID tenantId,
            String action,
            String module,
            String entity,
            UUID performedBy) {

        String sql =
                "INSERT INTO audit_logs " +
                        "(id, user_id, action, module, entity, entity_id, status, audit_timestamp) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                UUID.randomUUID(),
                performedBy,
                action,
                module,
                entity,
                tenantId.toString(),
                "SUCCESS",
                LocalDate.now()
        );
    }
}