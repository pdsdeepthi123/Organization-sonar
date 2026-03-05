package com.dtt.organization.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;


@Entity
@Table(name="software_licenses")
@NamedQuery(name="SoftwareLicenses.findAll", query="SELECT s FROM SoftwareLicenses s")
public class SoftwareLicenses implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "ouid")
	private String ouid;
	
	@Column(name = "software_name")
	private String appid;
	
	@Column(name="created_date_time")
	private String createdDateTime;
	
	@Column(name="updated_date_time")
	private String updatedDateTime;
	
	@Column(name="license_info")
	private String licenseInfo;
	
	@Column(name="issued_on")
	private String issuedOn;
	
	@Column(name="valid_upto")
	private String validUpTo;
	
	@Column(name="license_type")
	private String licenseType;
	
	@Column(name="last_activated")
	private String lastActivated;
	
	@Column(name="first_activated")
	private String firstActivated;

	@Column(name="licence_status")
	private String licenceStatus;
	
	@Column(name="application_name")
	private String applicationName;
	
	@Column(name="org_name")
	private String organizationName;
	
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
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(String updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
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
	public String getValidUpTo() {
		return validUpTo;
	}
	public void setValidUpTo(String validUpTo) {
		this.validUpTo = validUpTo;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getLastActivated() {
		return lastActivated;
	}
	public void setLastActivated(String lastActivated) {
		this.lastActivated = lastActivated;
	}
	public String getFirstActivated() {
		return firstActivated;
	}
	public void setFirstActivated(String firstActivated) {
		this.firstActivated = firstActivated;
	}
	public String getLicenceStatus() {
		return licenceStatus;
	}
	public void setLicenceStatus(String licenceStatus) {
		this.licenceStatus = licenceStatus;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	@Override
	public String toString() {
		return "SoftwareLicenses [id=" + id + ", ouid=" + ouid + ", appid=" + appid + ", createdDateTime="
				+ createdDateTime + ", updatedDateTime=" + updatedDateTime + ", licenseInfo=" + licenseInfo
				+ ", issuedOn=" + issuedOn + ", validUpTo=" + validUpTo + ", licenseType=" + licenseType
				+ ", lastActivated=" + lastActivated + ", firstActivated=" + firstActivated + ", licenceStatus="
				+ licenceStatus + ", applicationName=" + applicationName + ", organizationName=" + organizationName
				+ "]";
	}
	
	
	
}
