package com.tenant_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_branding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantBranding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tenant_id",
            nullable = false,
            unique = true
    )
    private Tenant tenant;

    @Column(name = "display_name", length = 100)
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}