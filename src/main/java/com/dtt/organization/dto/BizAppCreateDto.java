package com.dtt.organization.dto;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BizAppCreateDto {
    @JsonProperty("ApplicationType")
    private String applicationType;

    @JsonProperty("ApplicationName")
    private String applicationName;

    @JsonProperty("ApplicationUri")
    private String applicationUri;

    @JsonProperty("RedirectUri")
    private String redirectUri;

    @JsonProperty("GrantTypes")
    private String grantTypes;

    @JsonProperty("Scopes")
    private String scopes;

    @JsonProperty("GrantTypesList")
    private List<String> grantTypesList;

    @JsonProperty("ScopesList")
    private List<String> scopesList;

    @JsonProperty("LogoutUri")
    private String logoutUri;

    @JsonProperty("OrganizationId")
    private String organizationId;

    @JsonProperty("Base64Cert")
    private String base64Cert;

    @JsonProperty("AuthSchemaId")
    private String authSchemaId;

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationUri() {
        return applicationUri;
    }

    public void setApplicationUri(String applicationUri) {
        this.applicationUri = applicationUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public List<String> getGrantTypesList() {
        return grantTypesList;
    }

    public void setGrantTypesList(List<String> grantTypesList) {
        this.grantTypesList = grantTypesList;
    }

    public List<String> getScopesList() {
        return scopesList;
    }

    public void setScopesList(List<String> scopesList) {
        this.scopesList = scopesList;
    }

    public String getLogoutUri() {
        return logoutUri;
    }

    public void setLogoutUri(String logoutUri) {
        this.logoutUri = logoutUri;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getBase64Cert() {
        return base64Cert;
    }

    public void setBase64Cert(String base64Cert) {
        this.base64Cert = base64Cert;
    }

    public String getAuthSchemaId() {
        return authSchemaId;
    }

    public void setAuthSchemaId(String authSchemaId) {
        this.authSchemaId = authSchemaId;
    }

    @Override
    public String toString() {
        return "BizAppCreateDto{" +
                "applicationType='" + applicationType + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", applicationUri='" + applicationUri + '\'' +
                ", redirectUri='" + redirectUri + '\'' +
                ", grantTypes='" + grantTypes + '\'' +
                ", scopes='" + scopes + '\'' +
                ", grantTypesList=" + grantTypesList +
                ", scopesList=" + scopesList +
                ", logoutUri='" + logoutUri + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", base64Cert='" + base64Cert + '\'' +
                ", authSchemaId='" + authSchemaId + '\'' +
                '}';
    }
}