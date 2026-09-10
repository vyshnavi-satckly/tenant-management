package com.tenant_management.dto.response;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantIsolationStatusResponseDto {

    private UUID tenantId;

    private String databaseIsolationStatus;

    private String storageIsolationStatus;

    private String apiSecurityStatus;

    private String complianceStatus;

    private Boolean databaseIsolationEnabled;

    private Boolean storageIsolationEnabled;

    private Boolean apiTenantOnly;

    private Boolean crossTenantAccessEnabled;

    private Boolean privateNetworkEnabled;

    private Boolean ipWhitelistingEnabled;

    private String allowedIpAddress;
}



