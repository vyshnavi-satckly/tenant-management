package com.tenant_management.dto.request;

public class UpdateTenantRequest {
    private String tenantName; private String organizationName;
    private String subscriptionPlan; private String primaryAdminName;
    private String primaryAdminEmail; private String primaryAdminMobile;
    private String country; private String timeZone; private String language;
    public String getTenantName(){return tenantName;} public void setTenantName(String v){this.tenantName=v;}
    public String getOrganizationName(){return organizationName;} public void setOrganizationName(String v){this.organizationName=v;}
    public String getSubscriptionPlan(){return subscriptionPlan;} public void setSubscriptionPlan(String v){this.subscriptionPlan=v;}
    public String getPrimaryAdminName(){return primaryAdminName;} public void setPrimaryAdminName(String v){this.primaryAdminName=v;}
    public String getPrimaryAdminEmail(){return primaryAdminEmail;} public void setPrimaryAdminEmail(String v){this.primaryAdminEmail=v;}
    public String getPrimaryAdminMobile(){return primaryAdminMobile;} public void setPrimaryAdminMobile(String v){this.primaryAdminMobile=v;}
    public String getCountry(){return country;} public void setCountry(String v){this.country=v;}
    public String getTimeZone(){return timeZone;} public void setTimeZone(String v){this.timeZone=v;}
    public String getLanguage(){return language;} public void setLanguage(String v){this.language=v;}
}