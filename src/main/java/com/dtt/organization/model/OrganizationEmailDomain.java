package com.dtt.organization.model;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "org_email_domains")
@NamedQuery(name="OrganizationEmailDomain.findAll", query="SELECT o FROM OrganizationEmailDomain o")
public class OrganizationEmailDomain implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_domain_id")
    private int orgDomainId;

    @Column(name = "organization_uid")
    private String organizationUid;

    @Column(name = "email_domain")
    private String emailDomain;

    @Column(name = "status")
    private boolean status;

    @Column(name = "created_on")
    private String createdOn;

    @Column(name = "updated_on")
    private String updatedOn;

    public int getOrgDomainId() {
        return orgDomainId;
    }

    public void setOrgDomainId(int orgDomainId) {
        this.orgDomainId = orgDomainId;
    }

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return "OrganizationEmailDomain{" +
                "orgDomainId=" + orgDomainId +
                ", organizationUid='" + organizationUid + '\'' +
                ", emailDomain='" + emailDomain + '\'' +
                ", status=" + status +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }
}
