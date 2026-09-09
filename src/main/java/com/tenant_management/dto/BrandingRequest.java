package com.tenant_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandingRequest {

    @NotBlank(message = "Display name is required")
    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String displayName;

    @Size( max = 255, message = "Company tagline must not exceed 255 characters")
    private String companyTagline;

    @Size(max = 250, message = "Welcome message must not exceed 250 characters")
    private String welcomeMessage;

    @Pattern(regexp = "^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$",
        message = "Primary colour must be a valid hexadecimal colour")
    private String primaryColour;

    @Pattern( regexp = "^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$",
        message = "Secondary colour must be a valid hexadecimal colour")
    private String secondaryColour;

    @Pattern( regexp = "^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$",
        message = "Accent colour must be a valid hexadecimal colour")
    private String accentColour;

    @Size( max = 20, message = "Theme must not exceed 20 characters")
    private String theme;

    @Size(max = 200,message = "Footer text must not exceed 200 characters")
    private String footerText;

    @Size( max = 255, message = "Copyright must not exceed 255 characters")
    private String copyright;

    @Size( max = 500,message = "Favicon path must not exceed 500 characters")
    private String favicon;
}