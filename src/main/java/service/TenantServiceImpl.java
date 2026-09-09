package com.tenant_management.service;

import com.tenant_management.entity.Tenant;
import com.tenant_management.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    public TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Tenant createTenant(Tenant tenant) {

        if (tenantRepository.existsByTenantId(tenant.getTenantId())) {
            throw new RuntimeException("Tenant ID already exists");
        }

        if (tenantRepository.existsByTenantName(tenant.getTenantName())) {
            throw new RuntimeException("Tenant name already exists");
        }

        if (tenant.getDomainName() != null
                && tenantRepository.existsByDomainName(tenant.getDomainName())) {
            throw new RuntimeException("Domain name already exists");
        }

        tenant.setCreatedAt(LocalDateTime.now());
        tenant.setIsDeleted(false);

        if (tenant.getStatus() == null) {
            tenant.setStatus("ACTIVE");
        }

        return tenantRepository.save(tenant);
    }

    @Override
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Override
    public Tenant getTenantById(UUID id) {
        return tenantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tenant not found"));
    }
}