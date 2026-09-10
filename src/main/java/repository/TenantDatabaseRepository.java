package com.tenant_management.repository;

import com.tenant_management.entity.TenantDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantDatabaseRepository extends JpaRepository<TenantDatabase, UUID> {

}