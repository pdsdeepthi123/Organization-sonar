package com.dtt.organization.dto;

public class UpdateEmailDomainDto {
    private String organizationUid;

    private String emailDomain;

    public String getOrganizationUid() {
        return organizationUid;
    }

    public void setOrganizationUid(String organizationUid) {
        this.organizationUid = organizationUid;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    @Override
    public String toString() {
        return "UpdateEmailDomainDto{" +
                "organizationUid='" + organizationUid + '\'' +
                ", emailDomain='" + emailDomain + '\'' +
                '}';
    }
}
