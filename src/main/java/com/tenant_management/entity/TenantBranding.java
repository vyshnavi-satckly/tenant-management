package com.tenant_management.entity;

import com.tenant_management.dto.BrandingRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tenant_branding",
    uniqueConstraints = { @UniqueConstraint(name = "uk_tenant_branding_tenant",columnNames = "tenant_id") })
public class TenantBranding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id",referencedColumnName = "id", nullable = false,
        updatable = false,unique = true,
        foreignKey = @ForeignKey(name = "fk_tenant_branding_tenant"))
    private Tenant tenant;

    @Column(name = "display_name", nullable = false,length = 100)
    private String displayName;

    @Column(name = "company_tagline", length = 255)
    private String companyTagline;

    @Column(name = "company_logo", length = 500)
    private String companyLogo;

    @Column(name = "background_image", length = 500)
    private String backgroundImage;

    @Column(name = "welcome_message", length = 250)
    private String welcomeMessage;

    @Column(name = "primary_colour", length = 20)
    private String primaryColour;

    @Column(name = "secondary_colour", length = 20)
    private String secondaryColour;

    @Column(name = "accent_colour", length = 20)
    private String accentColour;

    @Column(name = "theme", length = 20)
    private String theme;

    @Column(name = "footer_text", length = 200)
    private String footerText;

    @Column(name = "copyright", length = 255)
    private String copyright;

    @Column(name = "favicon", length = 500)
    private String favicon;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by",nullable = false,updatable = false )
    private UUID createdBy;

    @Column( name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "updated_by",nullable = false)
    private UUID updatedBy;

    public static TenantBranding create( Tenant tenant,UUID actor,BrandingRequest request) {
    	
        Objects.requireNonNull(tenant, "Tenant must not be null");
        Objects.requireNonNull( actor,"Created-by user must not be null");

        Objects.requireNonNull( request, "Branding request must not be null");

        Instant currentTime = Instant.now();

        TenantBranding branding = new TenantBranding();
        branding.tenant = tenant;
        branding.createdAt = currentTime;
        branding.createdBy = actor;
        branding.updatedAt = currentTime;
        branding.updatedBy = actor;
        branding.applyBrandingValues(request);

        return branding;
    }

    public void update( UUID actor, BrandingRequest request ) {
    	
        Objects.requireNonNull( actor, "Updated-by user must not be null" );

        Objects.requireNonNull( request, "Branding request must not be null");

        applyBrandingValues(request);
        updateAuditFields(actor);
    }

    public void updateCompanyLogo( String companyLogo, UUID actor) {
    	
        Objects.requireNonNull(actor, "Updated-by user must not be null" );
        this.companyLogo = trimToNull(companyLogo);
        updateAuditFields(actor);
    }

    public void updateBackgroundImage(String backgroundImage, UUID actor) {
    	
        Objects.requireNonNull(actor,"Updated-by user must not be null");
        this.backgroundImage = trimToNull(backgroundImage);
        updateAuditFields(actor);
    }

    private void applyBrandingValues(BrandingRequest request) {
    	
        Objects.requireNonNull( request,"Branding request must not be null");

        this.displayName = request.getDisplayName().trim();

        this.companyTagline = trimToNull(request.getCompanyTagline());

        this.welcomeMessage =trimToNull(request.getWelcomeMessage());

        this.primaryColour = normalizeColour(request.getPrimaryColour());

        this.secondaryColour = normalizeColour(request.getSecondaryColour());

        this.accentColour =normalizeColour(request.getAccentColour());

        this.theme = trimToNull(request.getTheme());

        this.footerText =trimToNull(request.getFooterText());

        this.copyright = trimToNull(request.getCopyright());

        this.favicon =trimToNull(request.getFavicon());
    }
    private void updateAuditFields(UUID actor) {
        this.updatedBy = actor;
        this.updatedAt = Instant.now();
    }

    private String normalizeColour(String colour) {
        String normalizedColour = trimToNull(colour);

        return normalizedColour == null
            ? null
            : normalizedColour.toUpperCase(Locale.ROOT);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();

        return trimmedValue.isEmpty()
            ? null
            : trimmedValue;
    }
}