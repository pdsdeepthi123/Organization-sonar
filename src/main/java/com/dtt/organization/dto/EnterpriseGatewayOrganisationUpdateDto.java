package com.dtt.organization.dto;

public class EnterpriseGatewayOrganisationUpdateDto {

    private String organizationUid;

    private String spocUgpassEmail;

    private String agentUrl;


    public String getOrganizationUid() {
        return organizationUid;
    }

    public void setOrganizationUid(String organizationUid) {
        this.organizationUid = organizationUid;
    }

    public String getSpocUgpassEmail() {
        return spocUgpassEmail;
    }

    public void setSpocUgpassEmail(String spocUgpassEmail) {
        this.spocUgpassEmail = spocUgpassEmail;
    }

    public String getAgentUrl() {
        return agentUrl;
    }

    public void setAgentUrl(String agentUrl) {
        this.agentUrl = agentUrl;
    }

    @Override
    public String toString() {
        return "EnterpriseGatewayOrganisationUpdateDto{" +
                "organizationUid='" + organizationUid + '\'' +
                ", spocUgpassEmail='" + spocUgpassEmail + '\'' +
                ", agentUrl='" + agentUrl + '\'' +
                '}';
    }
}
