package com.dtt.organization.model;

import jakarta.persistence.*;

@Entity
@Table(name="org_buckets_config")
public class OrgBucketConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="org_id")
    private String orgId;

    @Column(name="org_name")
    private String orgName;

    @Column(name="app_id")
    private String appId;

    @Column(name="label")
    private String label;

    @Column(name="bucket_closing_message")
    private String bucketClosingMessage;

    @Column(name="created_on")
    private String createdOn;

    @Column(name="updated_on")
    private String updatedOn;

    @Column(name="status")
    private String status;

    @Column(name="additional_ds")
    private int additionalDs;

    @Column(name="additional_eds")
    private int additionalEds;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBucketClosingMessage() {
        return bucketClosingMessage;
    }

    public void setBucketClosingMessage(String bucketClosingMessage) {
        this.bucketClosingMessage = bucketClosingMessage;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getAdditionalDs() {
        return additionalDs;
    }

    public void setAdditionalDs(int additionalDs) {
        this.additionalDs = additionalDs;
    }

    public int getAdditionalEds() {
        return additionalEds;
    }

    public void setAdditionalEds(int additionalEds) {
        this.additionalEds = additionalEds;
    }

    @Override
    public String toString() {
        return "OrgBucketConfig{" +
                "id=" + id +
                ", orgId='" + orgId + '\'' +
                ", orgName='" + orgName + '\'' +
                ", appId='" + appId + '\'' +
                ", label='" + label + '\'' +
                ", bucketClosingMessage='" + bucketClosingMessage + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", status='" + status + '\'' +
                ", additionalDs=" + additionalDs +
                ", additionalEds=" + additionalEds +
                '}';
    }
}
