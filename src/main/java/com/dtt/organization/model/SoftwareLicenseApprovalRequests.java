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
@Table(name="software_license_approval_requests")
@NamedQuery(name="SoftwareLicenseApprovalRequests.findAll", query="SELECT s FROM SoftwareLicenseApprovalRequests s")
public class SoftwareLicenseApprovalRequests implements Serializable{

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
	
	@Column(name="approval_status")
	private String approvalStatus;
	
	@Column(name="updated_date_time")
	private String updatedDateTime;
	
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



	public String getApprovalStatus() {
		return approvalStatus;
	}



	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}



	public String getUpdatedDateTime() {
		return updatedDateTime;
	}



	public void setUpdatedDateTime(String updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}



	public String getLicenseType() {
		return licenseType;
	}



	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}



	@Override
	public String toString() {
		return "SoftwareLicenseApprovalRequests [id=" + id + ", ouid=" + ouid + ", appid=" + appid
				+ ", created_date_time=" + createdDateTime + ", approval_status=" + approvalStatus
				+ ", updated_date_time=" + updatedDateTime + ", license_type=" + licenseType + "]";
	}
}
