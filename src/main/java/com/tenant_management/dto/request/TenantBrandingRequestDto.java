package com.tenant_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class TenantBrandingRequestDto {
    @NotBlank(message = "Display name is required")
    @Size(max = 100, message = "Display name cannot exceed 100 characters")
    private String displayName;

    @Size(max = 255, message = "Company tagline cannot exceed 255 characters")
    private String companyTagline;

    @Size(max = 250, message = "Welcome message cannot exceed 250 characters")
    private String welcomeMessage;

    @Pattern(
            regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
            message = "Primary colour must be a valid hex value"
    )
    private String primaryColour;

    @Pattern(
            regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
            message = "Secondary colour must be a valid hex value"
    )
    private String secondaryColour;

    @Pattern(
            regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
            message = "Accent colour must be a valid hex value"
    )
    private String accentColour;

    @Size(max = 20)
    private String theme;

    @Size(max = 200, message = "Footer text cannot exceed 200 characters")
    private String footerText;

    @Size(max = 255)
    private String copyright;
}
