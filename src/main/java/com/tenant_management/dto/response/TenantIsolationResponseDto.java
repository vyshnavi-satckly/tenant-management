package com.tenant_management.dto.response;


import java.time.LocalDateTime;
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
public class TenantIsolationResponseDto {

    private UUID id;

    private UUID tenantId;

    private Boolean databaseIsolationEnabled;

    private Boolean storageIsolationEnabled;

    private Boolean apiTenantOnly;

    private Boolean crossTenantAccessEnabled;

    private Boolean privateNetworkEnabled;

    private Boolean ipWhitelistingEnabled;

    private String allowedIpAddress;

    private String databaseIsolationStatus;

    private String storageIsolationStatus;

    private String apiSecurityStatus;

    private String complianceStatus;

    private LocalDateTime createdAt;

    private UUID createdBy;

    private LocalDateTime updatedAt;

    private UUID updatedBy;
}


