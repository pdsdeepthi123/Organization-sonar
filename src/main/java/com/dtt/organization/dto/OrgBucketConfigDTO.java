package com.dtt.organization.dto;


import java.io.Serializable;

public class OrgBucketConfigDTO implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private int id;


    private String orgId;


    private String appId;


    private String label;


    private String bucketClosingMessage;


    private String createdOn;


    private String updatedOn;


    private String status;


    private int additionalDs;


    private int additionalEds;

    private String orgName;

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String toString() {
        return "OrgBucketConfigDTO{" +
                "id=" + id +
                ", orgId='" + orgId + '\'' +
                ", appId='" + appId + '\'' +
                ", label='" + label + '\'' +
                ", bucketClosingMessage='" + bucketClosingMessage + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", status='" + status + '\'' +
                ", additionalDs=" + additionalDs +
                ", additionalEds=" + additionalEds +
                ", orgName='" + orgName + '\'' +
                '}';
    }
}
