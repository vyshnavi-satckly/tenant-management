package com.tenant_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantBrandingResponseDto {

    private String tenantId;
    private String displayName;
    private String companyTagline;
    private String companyLogo;
    private String backgroundImage;
    private String welcomeMessage;
    private String primaryColour;
    private String secondaryColour;
    private String accentColour;
    private String theme;
    private String footerText;
    private String copyright;
    private String favicon;
}
