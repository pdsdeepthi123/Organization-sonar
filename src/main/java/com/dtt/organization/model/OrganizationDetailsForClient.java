package com.dtt.organization.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "view_client_apps")
@NamedQuery(name="OrganizationDetailsForClient.findAll", query="SELECT o FROM OrganizationDetailsForClient o")
public class OrganizationDetailsForClient implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "application_name")
    private String applicationName;

    @Column(name = "organization_uid")
    private String organizationUid;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "enable_post_paid_option")
    private boolean enablePostPaidOption;

    @Column(name = "organization_status")
    private String organizationStatus;

    @Column(name = "client_app_status")
    private String clientAppStatus;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getOrganizationUid() {
        return organizationUid;
    }

    public void setOrganizationUid(String organizationUid) {
        this.organizationUid = organizationUid;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public boolean isEnablePostPaidOption() {
        return enablePostPaidOption;
    }

    public void setEnablePostPaidOption(boolean enablePostPaidOption) {
        this.enablePostPaidOption = enablePostPaidOption;
    }

    public String getOrganizationStatus() {
        return organizationStatus;
    }

    public void setOrganizationStatus(String organizationStatus) {
        this.organizationStatus = organizationStatus;
    }

    public String getClientAppStatus() {
        return clientAppStatus;
    }

    public void setClientAppStatus(String clientAppStatus) {
        this.clientAppStatus = clientAppStatus;
    }

    @Override
    public String toString() {
        return "OrganizationDetailsForClient{" +
                "clientId='" + clientId + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", organizationUid='" + organizationUid + '\'' +
                ", orgName='" + orgName + '\'' +
                ", enablePostPaidOption=" + enablePostPaidOption +
                ", organizationStatus='" + organizationStatus + '\'' +
                ", clientAppStatus='" + clientAppStatus + '\'' +
                '}';
    }
}
