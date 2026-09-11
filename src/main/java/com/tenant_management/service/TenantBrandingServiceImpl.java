package com.tenant_management.service;

import com.tenant_management.dto.request.TenantBrandingRequestDto;
import com.tenant_management.dto.response.TenantBrandingFileUploadResponseDto;
import com.tenant_management.dto.response.TenantBrandingPreviewResponseDto;
import com.tenant_management.dto.response.TenantBrandingResponseDto;
import com.tenant_management.entity.Tenant;
import com.tenant_management.entity.TenantBranding;
import com.tenant_management.repository.TenantBrandingRepository;
import com.tenant_management.repository.TenantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TenantBrandingServiceImpl implements TenantBrandingService {

    private final TenantBrandingRepository tenantBrandingRepository;
    private final TenantRepository tenantRepository;
    private final FileStorageService fileStorageService;


    private static final long MAX_LOGO_SIZE = 5 * 1024 * 1024L;
    private static final long MAX_BACKGROUND_SIZE = 10 * 1024 * 1024L;

    private static final Set<String> LOGO_CONTENT_TYPES = Set.of(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "application/octet-stream",
            "image/svg+xml"
    );

    private static final Set<String> BACKGROUND_CONTENT_TYPES = Set.of(
            "image/png",
            "application/octet-stream",
            "image/jpg",
            "image/jpeg"
    );

    @Override
    public TenantBrandingResponseDto getBranding(String tenantId) {
        TenantBranding branding = getBrandingOrThrow(tenantId);
        return mapToBrandingResponse(branding);
    }

    @Override
    public TenantBrandingResponseDto updateBranding(String tenantId, @Valid TenantBrandingRequestDto request) {
        TenantBranding branding = getOrCreateBranding(tenantId);

        // This endpoint updates only normal JSON branding fields.
        // It does not update logo or background image.
        branding.setDisplayName(request.getDisplayName());
        branding.setCompanyTagline(request.getCompanyTagline());
        branding.setWelcomeMessage(request.getWelcomeMessage());
        branding.setPrimaryColour(request.getPrimaryColour());
        branding.setSecondaryColour(request.getSecondaryColour());
        branding.setAccentColour(request.getAccentColour());
        branding.setTheme(request.getTheme());
        branding.setFooterText(request.getFooterText());
        branding.setCopyright(request.getCopyright());

        TenantBranding savedBranding =
                tenantBrandingRepository.save(branding);

        return mapToBrandingResponse(savedBranding);
    }

    @Override
    public TenantBrandingFileUploadResponseDto uploadLogo(String tenantId, MultipartFile logo) {
        TenantBranding branding = getBrandingOrThrow(tenantId);
        System.out.println(logo.getContentType());
        validateFile(logo, MAX_LOGO_SIZE,LOGO_CONTENT_TYPES, "Logo");

        String logoUrl = fileStorageService.store(
                tenantId,
                "logo",
                logo
        );

        branding.setCompanyLogo(logoUrl);
        tenantBrandingRepository.save(branding);

        return new TenantBrandingFileUploadResponseDto(logoUrl,
                "Logo uploaded successfully");
    }

    @Override
    public TenantBrandingFileUploadResponseDto uploadBackground(String tenantId, MultipartFile background) {
        TenantBranding branding = getBrandingOrThrow(tenantId);

        validateFile(
                background,
                MAX_BACKGROUND_SIZE,
                BACKGROUND_CONTENT_TYPES,
                "Background image"
        );

        String backgroundUrl = fileStorageService.store(
                tenantId,
                "background",
                background
        );

        branding.setBackgroundImage(backgroundUrl);
        tenantBrandingRepository.save(branding);

        return new TenantBrandingFileUploadResponseDto(
                backgroundUrl,
                "Background image uploaded successfully"
        );
    }

    @Override
    public TenantBrandingPreviewResponseDto getPreview(String tenantId) {
        TenantBranding branding = getBrandingOrThrow(tenantId);

        return new TenantBrandingPreviewResponseDto(
                branding.getDisplayName(),
                branding.getCompanyLogo(),
                branding.getBackgroundImage(),
                branding.getWelcomeMessage(),
                branding.getPrimaryColour(),
                branding.getSecondaryColour(),
                branding.getAccentColour(),
                branding.getTheme()
        );

    }

    private Tenant getTenantOrThrow(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() ->
                        new RuntimeException("Tenant not found: " + tenantId)
                );
    }


    private TenantBranding getBrandingOrThrow(String tenantId) {
        Tenant tenant = getTenantOrThrow(tenantId);

        return tenantBrandingRepository.findByTenant(tenant)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Branding not found for tenant: " + tenantId
                        )
                );
    }

    private TenantBranding getOrCreateBranding(String tenantId) {
        Tenant tenant = getTenantOrThrow(tenantId);

        return tenantBrandingRepository.findByTenant(tenant)
                .orElseGet(() -> {
                    TenantBranding branding = new TenantBranding();
                    branding.setTenant(tenant);
                    return branding;
                });
    }


    private TenantBrandingResponseDto mapToBrandingResponse(
            TenantBranding branding) {

        return new TenantBrandingResponseDto(
                branding.getTenant().getTenantId(),
                branding.getDisplayName(),
                branding.getCompanyTagline(),
                branding.getCompanyLogo(),
                branding.getBackgroundImage(),
                branding.getWelcomeMessage(),
                branding.getPrimaryColour(),
                branding.getSecondaryColour(),
                branding.getAccentColour(),
                branding.getTheme(),
                branding.getFooterText(),
                branding.getCopyright(),
                branding.getFavicon()
        );
    }

    private void validateFile(
            MultipartFile file,
            long maximumSize,
            Set<String> allowedTypes,
            String fileLabel) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(fileLabel + " is required");
        }

        if (file.getSize() > maximumSize) {
            throw new RuntimeException(
                    fileLabel + " exceeds maximum allowed size"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new RuntimeException("Invalid " + fileLabel + " file type");
        }
    }

}
