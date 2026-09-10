package com.tenant_management.service;


import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenant_management.dto.request.TenantIsolationRequestDto;
import com.tenant_management.dto.response.TenantIsolationResponseDto;
import com.tenant_management.dto.response.TenantIsolationStatusResponseDto;
import com.tenant_management.entity.Tenant;
import com.tenant_management.entity.TenantIsolation;
import com.tenant_management.repository.TenantIsolationRepository;
import com.tenant_management.repository.TenantRepository;

@Service
@Transactional
public class TenantIsolationServiceImpl
        implements TenantIsolationService
{


    private final TenantRepository tenantRepository;

    private final TenantIsolationRepository tenantIsolationRepository;


    public TenantIsolationServiceImpl(
            TenantRepository tenantRepository,
            TenantIsolationRepository tenantIsolationRepository) 
    {

        this.tenantRepository = tenantRepository;
        this.tenantIsolationRepository = tenantIsolationRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public TenantIsolationResponseDto getIsolation(UUID tenantId) 
    {

        Tenant tenant = getTenant(tenantId);

        TenantIsolation isolation =
                tenantIsolationRepository
                        .findByTenantId(tenantId)
                        .orElse(null);

        if (isolation == null) 
        {
            return null;
        }

        calculateStatuses(isolation, tenant);

        return mapToResponse(isolation);
    }


    @Override
    public TenantIsolationResponseDto updateIsolation(
            UUID tenantId,
            TenantIsolationRequestDto request) 
    {

        Tenant tenant = getTenant(tenantId);

        validateRequest(request, tenant);

        TenantIsolation isolation =
                tenantIsolationRepository
                        .findByTenantId(tenantId)
                        .orElse(null);


        if (isolation == null) 
        {

            isolation = new TenantIsolation();

            isolation.setTenant(tenant);

            isolation.setCreatedAt(LocalDateTime.now());

        }


        isolation.setDatabaseIsolationEnabled(
                request.getDatabaseIsolationEnabled()
        );

        isolation.setStorageIsolationEnabled(
                request.getStorageIsolationEnabled()
        );

        isolation.setApiTenantOnly(
                request.getApiTenantOnly()
        );

        isolation.setCrossTenantAccessEnabled(
                request.getCrossTenantAccessEnabled()
        );

        isolation.setPrivateNetworkEnabled(
                request.getPrivateNetworkEnabled()
        );

        isolation.setIpWhitelistingEnabled(
                request.getIpWhitelistingEnabled()
        );


        if (Boolean.TRUE.equals(
                request.getIpWhitelistingEnabled())) {

            isolation.setAllowedIpAddress(
                    request.getAllowedIpAddress()
            );

        } 
        else 
        {

            isolation.setAllowedIpAddress(null);
        }


        isolation.setUpdatedAt(LocalDateTime.now());


        calculateStatuses(isolation, tenant);


        TenantIsolation saved =
                tenantIsolationRepository.save(isolation);


        return mapToResponse(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public TenantIsolationStatusResponseDto getIsolationStatus
    (
            UUID tenantId) {

        Tenant tenant = getTenant(tenantId);

        TenantIsolation isolation =
                tenantIsolationRepository
                        .findByTenantId(tenantId)
                        .orElse(null);


        if (isolation == null) 
        {
            return null;
        }


        calculateStatuses(isolation, tenant);


        TenantIsolationStatusResponseDto response =
                new TenantIsolationStatusResponseDto();


        response.setTenantId(tenantId);

        response.setDatabaseIsolationStatus(
                isolation.getDatabaseIsolationStatus()
        );

        response.setStorageIsolationStatus(
                isolation.getStorageIsolationStatus()
        );

        response.setApiSecurityStatus(
                isolation.getApiSecurityStatus()
        );

        response.setComplianceStatus(
                isolation.getComplianceStatus()
        );


        response.setDatabaseIsolationEnabled(
                isolation.getDatabaseIsolationEnabled()
        );

        response.setStorageIsolationEnabled(
                isolation.getStorageIsolationEnabled()
        );

        response.setApiTenantOnly(
                isolation.getApiTenantOnly()
        );

        response.setCrossTenantAccessEnabled(
                isolation.getCrossTenantAccessEnabled()
        );

        response.setPrivateNetworkEnabled(
                isolation.getPrivateNetworkEnabled()
        );

        response.setIpWhitelistingEnabled(
                isolation.getIpWhitelistingEnabled()
        );

        response.setAllowedIpAddress(
                isolation.getAllowedIpAddress()
        );


        return response;
    }


    private Tenant getTenant(UUID tenantId) 
    {

        return tenantRepository
                .findById(tenantId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Tenant not found: " + tenantId
                        )
                );
    }


    private void validateRequest(
            TenantIsolationRequestDto request,
            Tenant tenant) {


        if (request == null) 
        {

            throw new IllegalArgumentException(
                    "Isolation request cannot be null"
            );
        }


        if (request.getDatabaseIsolationEnabled() == null) 
        {

            throw new IllegalArgumentException(
                    "Database isolation setting is required"
            );
        }


        if (request.getStorageIsolationEnabled() == null) 
        {

            throw new IllegalArgumentException(
                    "Storage isolation setting is required"
            );
        }


        if (request.getApiTenantOnly() == null) 
        {

            throw new IllegalArgumentException(
                    "API tenant-only setting is required"
            );
        }


        if (request.getCrossTenantAccessEnabled() == null) 
        {

            throw new IllegalArgumentException(
                    "Cross tenant access setting is required"
            );
        }


        if (request.getPrivateNetworkEnabled() == null) 
        {

            throw new IllegalArgumentException(
                    "Private network setting is required"
            );
        }


        if (request.getIpWhitelistingEnabled() == null) 
        {

            throw new IllegalArgumentException(
                    "IP whitelisting setting is required"
            );
        }


        /*
         * Active tenant must always have
         * database isolation enabled.
         */

        if ("ACTIVE".equalsIgnoreCase(tenant.getStatus())
                && !Boolean.TRUE.equals(
                request.getDatabaseIsolationEnabled())) 
        {

            throw new IllegalArgumentException(
                    "Active tenant must have database isolation enabled"
            );
        }


        /*
         * Storage isolation is mandatory.
         */

        if (!Boolean.TRUE.equals(
                request.getStorageIsolationEnabled())) 
        {

            throw new IllegalArgumentException(
                    "Storage isolation must be enabled"
            );
        }


        /*
         * Active tenant must use tenant-only API access.
         */

        if ("ACTIVE".equalsIgnoreCase(tenant.getStatus())
                && !Boolean.TRUE.equals(
                request.getApiTenantOnly())) 
        {

            throw new IllegalArgumentException(
                    "Active tenant must have API tenant-only access enabled"
            );
        }


        /*
         * Cross tenant access must not be enabled.
         */

        if (Boolean.TRUE.equals(
                request.getCrossTenantAccessEnabled())) 
        {

            throw new IllegalArgumentException(
                    "Cross tenant access cannot be enabled"
            );
        }


        /*
         * Validate IP/CIDR when IP whitelisting
         * is enabled.
         */

        if (Boolean.TRUE.equals(
                request.getIpWhitelistingEnabled())) 
        {

            if (request.getAllowedIpAddress() == null
                    || request.getAllowedIpAddress()
                    .trim()
                    .isEmpty()) 
            {

                throw new IllegalArgumentException(
                        "Allowed IP address is required when IP whitelisting is enabled"
                );
            }


            if (!isValidIpOrCidr(
                    request.getAllowedIpAddress())) 
            {

                throw new IllegalArgumentException(
                        "Invalid IP address or CIDR: "
                                + request.getAllowedIpAddress()
                );
            }

        } 
        else 
        {

            if (request.getAllowedIpAddress() != null
                    && !request.getAllowedIpAddress()
                    .trim()
                    .isEmpty()) {

                throw new IllegalArgumentException(
                        "Allowed IP address must be empty when IP whitelisting is disabled"
                );
            }
        }
    }


    private void calculateStatuses(
            TenantIsolation isolation,
            Tenant tenant) 
    {


        /*
         * Database isolation status
         */

        if (Boolean.TRUE.equals(
                isolation.getDatabaseIsolationEnabled())) 
        {

            isolation.setDatabaseIsolationStatus(
                    "ENABLED"
            );

        } else {

            isolation.setDatabaseIsolationStatus(
                    "DISABLED"
            );
        }


        /*
         * Storage isolation status
         */

        if (Boolean.TRUE.equals(
                isolation.getStorageIsolationEnabled())) 
        {

            isolation.setStorageIsolationStatus(
                    "ENABLED"
            );

        } 
        else 
        {

            isolation.setStorageIsolationStatus(
                    "DISABLED"
            );
        }


        /*
         * API security status
         */

        if (Boolean.TRUE.equals(
                isolation.getApiTenantOnly())
                && Boolean.FALSE.equals(
                isolation.getCrossTenantAccessEnabled())) 
        {

            isolation.setApiSecurityStatus(
                    "SECURE"
            );

        } else {

            isolation.setApiSecurityStatus(
                    "INSECURE"
            );
        }


        /*
         * Compliance status
         */

        boolean databaseValid =
                Boolean.TRUE.equals(
                        isolation.getDatabaseIsolationEnabled()
                );


        boolean storageValid =
                Boolean.TRUE.equals(
                        isolation.getStorageIsolationEnabled()
                );


        boolean apiValid =
                Boolean.TRUE.equals(
                        isolation.getApiTenantOnly()
                )
                        && Boolean.FALSE.equals(
                        isolation.getCrossTenantAccessEnabled()
                );


        boolean ipValid = true;


        if (Boolean.TRUE.equals(
                isolation.getIpWhitelistingEnabled())) 
        {

            ipValid =
                    isolation.getAllowedIpAddress() != null
                            && !isolation.getAllowedIpAddress()
                            .trim()
                            .isEmpty()
                            && isValidIpOrCidr(
                            isolation.getAllowedIpAddress()
                    );
        }


        boolean activeTenantValid = true;


        if ("ACTIVE".equalsIgnoreCase(
                tenant.getStatus())) 
        {

            activeTenantValid =
                    Boolean.TRUE.equals(
                            isolation.getDatabaseIsolationEnabled()
                    )
                            && Boolean.TRUE.equals(
                            isolation.getApiTenantOnly()
                    );
        }


        if (databaseValid
                && storageValid
                && apiValid
                && ipValid
                && activeTenantValid) 
        {

            isolation.setComplianceStatus(
                    "COMPLIANT"
            );

        } else {

            isolation.setComplianceStatus(
                    "NON_COMPLIANT"
            );
        }
    }


    private boolean isValidIpOrCidr(String value) 
    {

        try {

            String ip = value.trim();

            /*
             * CIDR notation
             */

            if (ip.contains("/")) {

                String[] parts = ip.split("/");

                if (parts.length != 2) {
                    return false;
                }


                String address = parts[0];

                String prefix = parts[1];


                InetAddress inetAddress =
                        InetAddress.getByName(address);


                int prefixLength =
                        Integer.parseInt(prefix);


                int maxPrefix =
                        inetAddress
                                .getAddress()
                                .length * 8;


                return prefixLength >= 0
                        && prefixLength <= maxPrefix;
            }


            /*
             * Plain IP address
             */

            InetAddress address =
                    InetAddress.getByName(ip);


            String hostAddress =
                    address.getHostAddress();


            return hostAddress != null;

        } 
        catch (Exception e) 
        {

            return false;
        }
    }


    private TenantIsolationResponseDto mapToResponse(
            TenantIsolation isolation) {


        TenantIsolationResponseDto response =
                new TenantIsolationResponseDto();


        response.setId(
                isolation.getId()
        );

        response.setTenantId(
                isolation.getTenant().getId()
        );


        response.setDatabaseIsolationEnabled(
                isolation.getDatabaseIsolationEnabled()
        );

        response.setStorageIsolationEnabled(
                isolation.getStorageIsolationEnabled()
        );

        response.setApiTenantOnly(
                isolation.getApiTenantOnly()
        );

        response.setCrossTenantAccessEnabled(
                isolation.getCrossTenantAccessEnabled()
        );

        response.setPrivateNetworkEnabled(
                isolation.getPrivateNetworkEnabled()
        );

        response.setIpWhitelistingEnabled(
                isolation.getIpWhitelistingEnabled()
        );

        response.setAllowedIpAddress(
                isolation.getAllowedIpAddress()
        );


        response.setDatabaseIsolationStatus(
                isolation.getDatabaseIsolationStatus()
        );

        response.setStorageIsolationStatus(
                isolation.getStorageIsolationStatus()
        );

        response.setApiSecurityStatus(
                isolation.getApiSecurityStatus()
        );

        response.setComplianceStatus(
                isolation.getComplianceStatus()
        );


        response.setCreatedAt(
                isolation.getCreatedAt()
        );

        response.setCreatedBy(
                isolation.getCreatedBy()
        );

        response.setUpdatedAt(
                isolation.getUpdatedAt()
        );

        response.setUpdatedBy(
                isolation.getUpdatedBy()
        );


        return response;
    }
}
