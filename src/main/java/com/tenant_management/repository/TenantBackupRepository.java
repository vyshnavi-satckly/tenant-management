package com.tenant_management.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenant_management.entity.TenantBackup;
import com.tenant_management.entity.TenantBackupHistory;

public interface TenantBackupRepository extends JpaRepository<TenantBackup, UUID> {
	  Optional<TenantBackup> findByTenantId(UUID tenantId);
	  boolean existsByTenantId(UUID tenantId);
	}
	