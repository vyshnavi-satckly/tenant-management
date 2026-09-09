package com.tenant_management.dto;

import com.tenant_management.entity.TenantBranding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandingResponse {

    private UUID id;

    private UUID tenantId;

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

    private Instant createdAt;

    private UUID createdBy;

    private Instant updatedAt;

    private UUID updatedBy;

    public static BrandingResponse from(TenantBranding branding ) {
        Objects.requireNonNull( branding, "TenantBranding entity must not be null");

        Objects.requireNonNull(branding.getTenant(),"Tenant relationship must not be null" );

        return BrandingResponse.builder()
            .id(branding.getId())
            .tenantId(branding.getTenant().getId())
            .displayName(branding.getDisplayName())
            .companyTagline(branding.getCompanyTagline())
            .companyLogo(branding.getCompanyLogo())
            .backgroundImage(branding.getBackgroundImage())
            .welcomeMessage(branding.getWelcomeMessage())
            .primaryColour(branding.getPrimaryColour())
            .secondaryColour(branding.getSecondaryColour())
            .accentColour(branding.getAccentColour())
            .theme(branding.getTheme())
            .footerText(branding.getFooterText())
            .copyright(branding.getCopyright())
            .favicon(branding.getFavicon())
            .createdAt(branding.getCreatedAt())
            .createdBy(branding.getCreatedBy())
            .updatedAt(branding.getUpdatedAt())
            .updatedBy(branding.getUpdatedBy())
            .build();
    }
}