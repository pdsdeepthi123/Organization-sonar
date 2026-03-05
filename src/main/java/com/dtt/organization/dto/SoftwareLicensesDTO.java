package com.dtt.organization.dto;

import java.io.Serializable;

public class SoftwareLicensesDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private int id;

	private String ouid;

	private String appid;

	private String createdOn;

	private String updatedOn;

	private String licenseInfo;

	private String issuedOn;

	private String validUpto;

	private String licenseType;

	private String applicationType;

	private String privateKey;

	private String clientassertiontype;

	private String clientId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOuid() {
		return ouid;
	}

	public void setOuid(String ouid) {
		this.ouid = ouid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
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

	public String getLicenseInfo() {
		return licenseInfo;
	}

	public void setLicenseInfo(String licenseInfo) {
		this.licenseInfo = licenseInfo;
	}

	public String getIssuedOn() {
		return issuedOn;
	}

	public void setIssuedOn(String issuedOn) {
		this.issuedOn = issuedOn;
	}

	public String getValidUpto() {
		return validUpto;
	}

	public void setValidUpto(String validUpto) {
		this.validUpto = validUpto;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getClientassertiontype() {
		return clientassertiontype;
	}

	public void setClientassertiontype(String clientassertiontype) {
		this.clientassertiontype = clientassertiontype;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "SoftwareLicensesDTO [id=" + id + ", ouid=" + ouid + ", appid=" + appid + ", createdOn=" + createdOn
				+ ", updatedOn=" + updatedOn + ", licenseInfo=" + licenseInfo + ", issuedOn=" + issuedOn
				+ ", validUpto=" + validUpto + ", licenseType=" + licenseType + ", applicationType=" + applicationType
				+ ", privateKey=" + privateKey + ", clientassertiontype=" + clientassertiontype + ", clientId="
				+ clientId + "]";
	}


	public String licenseInfoNew(String orgId,String licenseType, String macAddress,String clientId) {
		return "{" + "\"clientId\" : \"" + clientId + "\"," + "\"orgId\" : \"" + orgId + "\","
				+ "\"macAddress\" : \"" + macAddress + "\"," + "\"licenseType\" : \"" + licenseType + "\"}";
	}


}