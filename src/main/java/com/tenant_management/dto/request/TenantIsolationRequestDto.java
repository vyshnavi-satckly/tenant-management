package com.tenant_management.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
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
public class TenantIsolationRequestDto {

    private Boolean databaseIsolationEnabled;

    private Boolean storageIsolationEnabled;

    private Boolean apiTenantOnly;

    private Boolean crossTenantAccessEnabled;

    private Boolean privateNetworkEnabled;

    private Boolean ipWhitelistingEnabled;

    @Size(
            max = 255,
            message = "Allowed IP address cannot exceed 255 characters"
    )
    private String allowedIpAddress;

    /**
     * If IP whitelisting is enabled,
     * allowed IP/CIDR must be provided.
     */
    @AssertTrue(
            message = "Allowed IP address is required when IP whitelisting is enabled"
    )
    public boolean isIpAddressProvided() {

        if (Boolean.TRUE.equals(ipWhitelistingEnabled)) {
            return allowedIpAddress != null
                    && !allowedIpAddress.trim().isEmpty();
        }

        return true;
    }
}