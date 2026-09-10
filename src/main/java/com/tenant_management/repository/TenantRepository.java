package com.tenant_management.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenant_management.entity.Tenant;

public interface TenantRepository
        extends JpaRepository<Tenant, UUID>{
	
}


	