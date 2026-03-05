package com.dtt.organization.model;

import jakarta.persistence.*;

@Entity
@Table(name = "org_client_app_config")
public class OrgClientAppConfig {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="org_id")
    private String orgId;

    @Column(name="app_id")
    private String appId;

    @Column(name="config_value")
    private String configValue;

    @Column(name="status")
    private String status;

    @Column(name="created_on")
    private String createdOn;

    @Column(name="updated_on")
    private String updatedOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        return "OrgClientAppConfig{" +
                "id='" + id + '\'' +
                ", orgId='" + orgId + '\'' +
                ", appId='" + appId + '\'' +
                ", configValue='" + configValue + '\'' +
                ", status='" + status + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", UpdatedOn='" + updatedOn + '\'' +
                '}';
    }
}
