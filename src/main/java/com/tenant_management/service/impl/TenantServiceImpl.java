package com.tenant_management.service.impl;

import com.tenant_management.dto.request.CreateTenantRequest;
import com.tenant_management.dto.request.UpdateTenantRequest;
import com.tenant_management.dto.response.TenantResponse;
import com.tenant_management.entity.AuditLog;
import com.tenant_management.entity.Tenant;
import com.tenant_management.repository.AuditLogRepository;
import com.tenant_management.repository.TenantRepository;
import com.tenant_management.service.TenantService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository repo;
    private final AuditLogRepository auditRepo;

    public TenantServiceImpl(TenantRepository repo, AuditLogRepository auditRepo){
        this.repo=repo;
        this.auditRepo=auditRepo;
    }

    private void saveAudit(String entityId, String action, String status) {
        AuditLog log = new AuditLog();
        log.setId(UUID.randomUUID());
        log.setEntity("tenants");
        log.setEntityId(entityId);
        log.setAction(action);
        log.setModule("tenant-management");
        log.setStatus(status);
        log.setAuditTimestamp(LocalDate.now());
        auditRepo.save(log);
    }

    // Helper to convert Entity to Response DTO for 1A
    private TenantResponse mapToResponse(Tenant t) {
        TenantResponse r = new TenantResponse();
        r.setId(t.getId());
        r.setTenantId(t.getTenantId());
        r.setTenantName(t.getTenantName());
        r.setOrganizationName(t.getOrganizationName());
        r.setDomainName(t.getDomainName());
        r.setSubscriptionPlan(t.getSubscriptionPlan());
        r.setStatus(t.getStatus());
        r.setPrimaryAdminName(t.getPrimaryAdminName());
        r.setPrimaryAdminEmail(t.getPrimaryAdminEmail());
        r.setCountry(t.getCountry());
        r.setCreatedAt(t.getCreatedAt());
        return r;
    }

    @Override
    public Tenant createTenant(CreateTenantRequest req){
        if(repo.existsByTenantName(req.getTenantName())) throw new RuntimeException("Tenant name exists");
        if(repo.existsByDomainName(req.getDomainName())) throw new RuntimeException("Domain exists");
        Tenant t = new Tenant();
        t.setTenantId("TENANT-" + UUID.randomUUID().toString().substring(0,8).toUpperCase());
        t.setTenantName(req.getTenantName());
        t.setOrganizationName(req.getOrganizationName());
        t.setDomainName(req.getDomainName());
        t.setSubscriptionPlan(req.getSubscriptionPlan());
        t.setStatus("ACTIVE");
        t.setPrimaryAdminName(req.getPrimaryAdminName());
        t.setPrimaryAdminEmail(req.getPrimaryAdminEmail());
        t.setPrimaryAdminMobile(req.getPrimaryAdminMobile());
        t.setCountry(req.getCountry());
        t.setTimeZone(req.getTimeZone());
        t.setLanguage(req.getLanguage());
        t.setCreatedAt(LocalDateTime.now());
        t.setUpdatedAt(LocalDateTime.now());
        t.setIsDeleted(false);
        Tenant saved = repo.save(t);
        saveAudit(saved.getTenantId(), "CREATE", saved.getStatus());
        return saved;
    }

    @Override public Tenant updateTenant(String tenantId, UpdateTenantRequest req){
        Tenant t = repo.findByTenantId(tenantId).orElseThrow(() -> new RuntimeException("Tenant not found"));
        if(req.getTenantName()!=null) t.setTenantName(req.getTenantName());
        if(req.getOrganizationName()!=null) t.setOrganizationName(req.getOrganizationName());
        if(req.getSubscriptionPlan()!=null) t.setSubscriptionPlan(req.getSubscriptionPlan());
        if(req.getPrimaryAdminName()!=null) t.setPrimaryAdminName(req.getPrimaryAdminName());
        if(req.getPrimaryAdminEmail()!=null) t.setPrimaryAdminEmail(req.getPrimaryAdminEmail());
        if(req.getPrimaryAdminMobile()!=null) t.setPrimaryAdminMobile(req.getPrimaryAdminMobile());
        if(req.getCountry()!=null) t.setCountry(req.getCountry());
        if(req.getTimeZone()!=null) t.setTimeZone(req.getTimeZone());
        if(req.getLanguage()!=null) t.setLanguage(req.getLanguage());
        t.setUpdatedAt(LocalDateTime.now());
        Tenant saved = repo.save(t);
        saveAudit(saved.getTenantId(), "UPDATE", saved.getStatus());
        return saved;
    }

    @Override public Tenant enableTenant(String tenantId){
        Tenant t = repo.findByTenantId(tenantId).orElseThrow();
        t.setStatus("ACTIVE"); t.setUpdatedAt(LocalDateTime.now());
        Tenant saved = repo.save(t);
        saveAudit(saved.getTenantId(), "ENABLE", "ACTIVE");
        return saved;
    }

    @Override public Tenant disableTenant(String tenantId){
        Tenant t = repo.findByTenantId(tenantId).orElseThrow();
        t.setStatus("INACTIVE"); t.setUpdatedAt(LocalDateTime.now());
        Tenant saved = repo.save(t);
        saveAudit(saved.getTenantId(), "DISABLE", "INACTIVE");
        return saved;
    }

    @Override public List<Tenant> exportTenants(){return repo.findByIsDeletedFalse();}
    @Override public List<Tenant> getAllTenants(){return repo.findByIsDeletedFalse();}
    @Override public Tenant getTenantById(String tenantId){return repo.findByTenantId(tenantId).orElseThrow();}

    // ===== TEAM 1A - APIS =====
    @Override
    public List<TenantResponse> searchTenants(String keyword) {
        List<Tenant> tenants = repo.searchTenants(keyword);
        return tenants.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<TenantResponse> filterTenants(String status, String subscriptionPlan, String country) {
        // If all null, return all
        if(status == null && subscriptionPlan == null && country == null){
            return repo.findByIsDeletedFalse().stream().map(this::mapToResponse).collect(Collectors.toList());
        }
        List<Tenant> tenants = repo.filterTenants(status, subscriptionPlan, country);
        return tenants.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getOverview() {
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalTenants", repo.countByIsDeletedFalse());
        overview.put("activeTenants", repo.countByStatusAndIsDeletedFalse("ACTIVE"));
        overview.put("inactiveTenants", repo.countByStatusAndIsDeletedFalse("INACTIVE"));
        overview.put("total", repo.count());
        return overview;
    }
}