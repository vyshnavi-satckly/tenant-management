package com.tenant_management.controller;

import com.tenant_management.dto.request.TenantBrandingRequestDto;
import com.tenant_management.dto.response.TenantBrandingFileUploadResponseDto;
import com.tenant_management.dto.response.TenantBrandingPreviewResponseDto;
import com.tenant_management.dto.response.TenantBrandingResponseDto;
import com.tenant_management.service.TenantBrandingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tenants/{tenantId}/branding")
@RequiredArgsConstructor
public class TenantBrandingController {

    private final TenantBrandingService tenantBrandingService;

    @GetMapping
    public ResponseEntity<TenantBrandingResponseDto> getBranding(
            @PathVariable String tenantId) {

        TenantBrandingResponseDto response =
                tenantBrandingService.getBranding(tenantId);

        return ResponseEntity.ok(response);
    }
    @PutMapping
    public ResponseEntity<TenantBrandingResponseDto> updateBranding(
            @PathVariable String tenantId,
            @Valid @RequestBody TenantBrandingRequestDto request) {

        TenantBrandingResponseDto response =
                tenantBrandingService.updateBranding(tenantId, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<TenantBrandingFileUploadResponseDto> uploadLogo(
            @PathVariable String tenantId,
            @RequestParam("logo") MultipartFile logo) {

        TenantBrandingFileUploadResponseDto response =
                tenantBrandingService.uploadLogo(tenantId, logo);

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/background",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<TenantBrandingFileUploadResponseDto> uploadBackground(
            @PathVariable String tenantId,
            @RequestParam("background") MultipartFile background) {

        TenantBrandingFileUploadResponseDto response =
                tenantBrandingService.uploadBackground(tenantId, background);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/preview")
    public ResponseEntity<TenantBrandingPreviewResponseDto> getPreview(
            @PathVariable String tenantId) {

        TenantBrandingPreviewResponseDto response =
                tenantBrandingService.getPreview(tenantId);

        return ResponseEntity.ok(response);
    }

}
