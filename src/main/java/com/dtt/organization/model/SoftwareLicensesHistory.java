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
@Table(name="software_licenses_history")
@NamedQuery(name="SoftwareLicensesHistory.findAll", query="SELECT s FROM SoftwareLicensesHistory s")
public class SoftwareLicensesHistory implements Serializable{

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
	private String validUpto;
	
	@Column(name="license_type")
	private String licenseType;

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

	@Override
	public String toString() {
		return "SoftwareLicensesHistory{" +
				"id=" + id +
				", ouid='" + ouid + '\'' +
				", appid='" + appid + '\'' +
				", createdDateTime='" + createdDateTime + '\'' +
				", updatedDateTime='" + updatedDateTime + '\'' +
				", licenseInfo='" + licenseInfo + '\'' +
				", issuedOn='" + issuedOn + '\'' +
				", validUpto='" + validUpto + '\'' +
				", licenseType='" + licenseType + '\'' +
				'}';
	}
}
