
package com.dtt.organization.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "organization_certificate_life_cycle")
public class OrganizationCertificateLifeCycle implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The certificate management id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "organization_certificate_life_cycle_id", length = 11)
	private int organizationCertificateLifeCycleId;

	/** The organization id. */
	@Column(name = "organization_uid", nullable = false, length = 36)
	private String organizationUid;

	/** The certificate serial number. */
	@Column(name = "certificate_serial_number", nullable = false, length = 32)
	private String certificateSerialNumber;

	/** The certificate status. */
	@Column(name = "certificate_status", nullable = false, length = 16)
	private String certificateStatus;

	/** The revoked reason. */
	@Column(name = "revocation_reason", length = 128)
	private String revokedReason;

	/** The creation date. */
	@Column(name = "created_date", nullable = false)
	private Date creationDate;

	
	/** The certificate type. */
	@Column(name="certificate_type", nullable = false,length = 16)
	private String certificateType;
	/**
	 * Gets the organization certificate life cycle id.
	 *
	 * @return the organization certificate life cycle id
	 */
	public int getOrganizationCertificateLifeCycleId() {
		return organizationCertificateLifeCycleId;
	}

	/**
	 * Sets the organization certificate life cycle id.
	 *
	 * @param organizationCertificateLifeCycleId the new organization certificate
	 *                                           life cycle id
	 */
	public void setOrganizationCertificateLifeCycleId(int organizationCertificateLifeCycleId) {
		this.organizationCertificateLifeCycleId = organizationCertificateLifeCycleId;
	}

	/**
	 * Gets the organization id.
	 *
	 * @return the organization id
	 */
	public String getOrganizationUid() {
		return organizationUid;
	}

	
	/**
	 * Sets the organization id.
	 *
	 * @param organizationId the new organization id
	 */
	
	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	/**
	 * Gets the certificate serial number.
	 *
	 * @return the certificate serial number
	 */
	public String getCertificateSerialNumber() {
		return certificateSerialNumber;
	}

	/**
	 * Sets the certificate serial number.
	 *
	 * @param certificateSerialNumber the new certificate serial number
	 */
	public void setCertificateSerialNumber(String certificateSerialNumber) {
		this.certificateSerialNumber = certificateSerialNumber;
	}

	/**
	 * Gets the certificate status.
	 *
	 * @return the certificate status
	 */
	public String getCertificateStatus() {
		return certificateStatus;
	}

	/**
	 * Sets the certificate status.
	 *
	 * @param certificateStatus the new certificate status
	 */
	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	/**
	 * Gets the revoked reason.
	 *
	 * @return the revoked reason
	 */
	public String getRevokedReason() {
		return revokedReason;
	}

	/**
	 * Sets the revoked reason.
	 *
	 * @param revokedReason the new revoked reason
	 */
	public void setRevokedReason(String revokedReason) {
		this.revokedReason = revokedReason;
	}

	/**
	 * Gets the creation date.
	 *
	 * @return the creation date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date.
	 *
	 * @param creationDate the new creation date
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	@Override
	public String toString() {
		return "OrganizationCertificateLifeCycle [organizationCertificateLifeCycleId="
				+ organizationCertificateLifeCycleId + ", organizationUid=" + organizationUid
				+ ", certificateSerialNumber=" + certificateSerialNumber + ", certificateStatus=" + certificateStatus
				+ ", revokedReason=" + revokedReason + ", creationDate=" + creationDate + ", certificateType="
				+ certificateType + "]";
	}


}
