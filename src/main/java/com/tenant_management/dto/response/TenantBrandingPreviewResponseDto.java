package com.tenant_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantBrandingPreviewResponseDto {
    private String displayName;
    private String logoUrl;
    private String backgroundImageUrl;
    private String welcomeMessage;
    private String primaryColour;
    private String secondaryColour;
    private String accentColour;
    private String theme;
}
