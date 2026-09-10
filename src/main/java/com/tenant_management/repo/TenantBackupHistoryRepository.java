package com.tenant_management.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenant_management.entity.TenantBackupHistory;

public interface TenantBackupHistoryRepository extends JpaRepository<TenantBackupHistory, UUID> {
	  List<TenantBackupHistory> findByTenantIdOrderByBackupDateTimeDesc(UUID tenantId);
	  Optional<TenantBackupHistory> findByTenantIdAndBackupVersion(UUID tenantId, String backupVersion);
	}