package com.tenant_management.dto.request;

import jakarta.validation.constraints.*;

public class CreateTenantRequest {
    @NotBlank @Size(max=150) private String tenantName;
    @NotBlank @Size(max=200) private String organizationName;
    @NotBlank @Size(max=255) private String domainName;
    @NotBlank private String subscriptionPlan;
    @NotBlank @Size(max=150) private String primaryAdminName;
    @NotBlank @Email private String primaryAdminEmail;
    @NotBlank private String primaryAdminMobile;
    @NotBlank private String country;
    @NotBlank private String timeZone;
    @NotBlank private String language;

    // Getters & Setters
    public String getTenantName(){return tenantName;} public void setTenantName(String v){this.tenantName=v;}
    public String getOrganizationName(){return organizationName;} public void setOrganizationName(String v){this.organizationName=v;}
    public String getDomainName(){return domainName;} public void setDomainName(String v){this.domainName=v;}
    public String getSubscriptionPlan(){return subscriptionPlan;} public void setSubscriptionPlan(String v){this.subscriptionPlan=v;}
    public String getPrimaryAdminName(){return primaryAdminName;} public void setPrimaryAdminName(String v){this.primaryAdminName=v;}
    public String getPrimaryAdminEmail(){return primaryAdminEmail;} public void setPrimaryAdminEmail(String v){this.primaryAdminEmail=v;}
    public String getPrimaryAdminMobile(){return primaryAdminMobile;} public void setPrimaryAdminMobile(String v){this.primaryAdminMobile=v;}
    public String getCountry(){return country;} public void setCountry(String v){this.country=v;}
    public String getTimeZone(){return timeZone;} public void setTimeZone(String v){this.timeZone=v;}
    public String getLanguage(){return language;} public void setLanguage(String v){this.language=v;}
}