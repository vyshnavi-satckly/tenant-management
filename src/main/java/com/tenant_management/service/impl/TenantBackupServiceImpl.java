package com.tenant_management.service.impl;

import com.tenant_management.dto.request.TenantBackupConfigRequestDto;
import com.tenant_management.dto.response.TenantBackupHistoryResponseDto;
import com.tenant_management.dto.response.TenantBackupResponseDto;
import com.tenant_management.entity.TenantBackup;
import com.tenant_management.entity.TenantBackupHistory;
import com.tenant_management.repository.TenantBackupHistoryRepository;
import com.tenant_management.repository.TenantBackupRepository;
import com.tenant_management.repository.TenantRepository;
import com.tenant_management.service.TenantBackupService;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantBackupServiceImpl implements TenantBackupService {

    private final TenantRepository tenantRepository;
    private final TenantBackupRepository backupRepository;
    private final TenantBackupHistoryRepository historyRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public TenantBackupResponseDto getBackupConfig(UUID tenantId) {
        validateTenantExists(tenantId);

        TenantBackup backup = backupRepository.findByTenantId(tenantId)
                .orElseGet(() -> createDefaultConfig(tenantId));

        return mapToResponse(backup);
    }

    @Override
    @Transactional
    public TenantBackupResponseDto updateBackupConfig(UUID tenantId, TenantBackupConfigRequestDto request, UUID updatedBy) {
        validateTenantExists(tenantId);

        if (Boolean.TRUE.equals(request.getAutoBackupEnabled()) && request.getBackupTime() == null) {
            throw new IllegalArgumentException("Backup time is required when auto backup is enabled");
        }

        TenantBackup backup = backupRepository.findByTenantId(tenantId)
                .orElseGet(() -> TenantBackup.builder()
                        .tenantId(tenantId)
                        .createdBy(updatedBy)
                        .build());

        backup.setAutoBackupEnabled(request.getAutoBackupEnabled());
        backup.setBackupFrequency(request.getBackupFrequency());
        backup.setBackupTime(request.getBackupTime());
        backup.setRetentionPeriodDays(request.getRetentionPeriodDays());
        backup.setUpdatedBy(updatedBy);

        TenantBackup saved = backupRepository.save(backup);
        logAudit(tenantId, "UPDATE_BACKUP_CONFIG", updatedBy);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public TenantBackupHistoryResponseDto triggerManualBackup(UUID tenantId, UUID triggeredBy) {
        validateTenantExists(tenantId);

        String version = "v-" + System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();

        TenantBackupHistory history = TenantBackupHistory.builder()
                .tenantId(tenantId)
                .backupDateTime(now)
                .backupType("MANUAL")
                .backupStatus("COMPLETED")
                .backupLocation("/backups/" + tenantId + "/" + version + ".bak")
                .backupVersion(version)
                .createdBy(triggeredBy)
                .build();

        TenantBackupHistory savedHistory = historyRepository.save(history);

        TenantBackup backup = backupRepository.findByTenantId(tenantId).orElse(null);
        if (backup != null) {
            backup.setLastBackup(now);
            backup.setCurrentStatus("SUCCESS");
            backupRepository.save(backup);
        }

        logAudit(tenantId, "TRIGGER_MANUAL_BACKUP", triggeredBy);
        return mapToHistoryResponse(savedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantBackupHistoryResponseDto> getBackupHistory(UUID tenantId) {
        validateTenantExists(tenantId);
        return historyRepository.findByTenantIdOrderByBackupDateTimeDesc(tenantId)
                .stream()
                .map(this::mapToHistoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TenantBackupHistoryResponseDto restoreBackup(UUID tenantId, String backupVersion, UUID restoredBy) {
        validateTenantExists(tenantId);

        TenantBackupHistory history = historyRepository.findByTenantIdAndBackupVersion(tenantId, backupVersion)
                .orElseThrow(() -> new IllegalArgumentException("Invalid backup version provided: " + backupVersion));

        logAudit(tenantId, "RESTORE_BACKUP", restoredBy);
        return mapToHistoryResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantBackupHistoryResponseDto> getBackupLogs(UUID tenantId) {
        return getBackupHistory(tenantId);
    }

    private void validateTenantExists(UUID tenantId) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new IllegalArgumentException("Tenant does not exist with ID: " + tenantId);
        }
    }

    private TenantBackup createDefaultConfig(UUID tenantId) {
        TenantBackup defaultConfig = TenantBackup.builder()
                .tenantId(tenantId)
                .autoBackupEnabled(false)
                .retentionPeriodDays(30)
                .currentStatus("NOT_CONFIGURED")
                .build();
        return backupRepository.save(defaultConfig);
    }

    private void logAudit(UUID tenantId, String action, UUID userId) {
        String sql = "INSERT INTO audit_logs (id, user_id, action, module, entity, entity_id, status, audit_timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        jdbcTemplate.update(
            sql, 
            UUID.randomUUID(), 
            userId, 
            action, 
            "TENANT_BACKUP", 
            "TENANT", 
            tenantId.toString(), 
            "SUCCESS"
        );
    }

    private TenantBackupResponseDto mapToResponse(TenantBackup b) {
        return TenantBackupResponseDto.builder()
                .id(b.getId())
                .tenantId(b.getTenantId())
                .autoBackupEnabled(b.getAutoBackupEnabled())
                .backupFrequency(b.getBackupFrequency())
                .backupTime(b.getBackupTime())
                .retentionPeriodDays(b.getRetentionPeriodDays())
                .lastBackup(b.getLastBackup())
                .nextScheduledBackup(b.getNextScheduledBackup())
                .currentStatus(b.getCurrentStatus())
                .build();
    }

    private TenantBackupHistoryResponseDto mapToHistoryResponse(TenantBackupHistory h) {
        return TenantBackupHistoryResponseDto.builder()
                .id(h.getId())
                .tenantId(h.getTenantId())
                .backupDateTime(h.getBackupDateTime())
                .backupType(h.getBackupType())
                .backupStatus(h.getBackupStatus())
                .backupLocation(h.getBackupLocation())
                .backupVersion(h.getBackupVersion())
                .build();
    }
}