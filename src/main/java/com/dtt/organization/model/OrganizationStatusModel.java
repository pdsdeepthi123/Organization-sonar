package com.dtt.organization.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "organization_status")

public class OrganizationStatusModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The organization status id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "organization_status_id")
	private int organizationStatusId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;

	/** The otp verified status. */
	@Column(name = "otp_verified_status")
	private String otpVerifiedStatus;

	/** The organization status. */
	@Column(name = "organization_status")
	private String organizationStatus;

	/** The organization status description. */
	@Column(name = "organization_status_description")
	private String organizationStatusDescription;

	/** The organization uid. */
	@Column(name = "organization_uid")
	private String organizationUid;

	/** The updated date. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date updatedDate;

	public int getOrganizationStatusId() {
		return organizationStatusId;
	}

	public void setOrganizationStatusId(int organizationStatusId) {
		this.organizationStatusId = organizationStatusId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getOtpVerifiedStatus() {
		return otpVerifiedStatus;
	}

	public void setOtpVerifiedStatus(String otpVerifiedStatus) {
		this.otpVerifiedStatus = otpVerifiedStatus;
	}

	public String getOrganizationStatus() {
		return organizationStatus;
	}

	public void setOrganizationStatus(String organizationStatus) {
		this.organizationStatus = organizationStatus;
	}

	public String getOrganizationStatusDescription() {
		return organizationStatusDescription;
	}

	public void setOrganizationStatusDescription(String organizationStatusDescription) {
		this.organizationStatusDescription = organizationStatusDescription;
	}

	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "OrganizationStatus [organizationStatusId=" + organizationStatusId + ", createdDate=" + createdDate
				+ ", otpVerifiedStatus=" + otpVerifiedStatus + ", organizationStatus=" + organizationStatus
				+ ", organizationStatusDescription=" + organizationStatusDescription + ", organizationUid="
				+ organizationUid + ", updatedDate=" + updatedDate + "]";
	}

}
