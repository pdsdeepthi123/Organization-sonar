package com.dtt.organization.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "wallet_certificate")
public class WalletSignCertificate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The certificate serial number. */
	@Id
	@Column(name ="certificate_serial_number")
	private String certificateSerialNumber;
	
	/** The organization id. */
	@Column(name = "organization_uid", nullable = false, length = 36)
	private String organizationUid;
	
	/** The pki key id. */
	@Column(name = "pki_key_id", nullable = false, unique = true, length = 36)
	private String pkiKeyId;
	
	/** The certificate data. */
	@Column(name = "certificate_data", nullable = false, unique = true, length = 4096)
	private String certificateData;
	
	/** The certificate data. */
	@Column(name = "wrapped_key", nullable = false, unique = true, length = 5000)
	private String wrappedKey;
	
	/** The start date. */
	@Column(name = "certificate_issue_date", nullable = false)
	private Date certificateStartDate;
	
	/** The end date. */
	@Column(name = "cerificate_expiry_date", nullable = false)
	private Date certificateEndDate;
	
	/** The certificate status. */
	@Column(name = "certificate_status", nullable = false, length = 16)
	// @Enumerated(EnumType.STRING)
	private String certificateStatus;
	
	/** The revocation reason. */
	@Column(name = "remarks", length = 128)
	// @Enumerated(EnumType.STRING)
	private String revocationReason;
	
	/** The creation date. */
	@Column(name = "created_date", nullable = false)
	private Date creationDate;

	/** The updated date. */
	@Column(name = "updated_date")
	private Date updatedDate;
	
	/** The certificate type. */
	@Column(name = "certificate_type", nullable = false, length = 16)
	private String certificateType;
	
	@Column(name = "transaction_reference_id", length=40)
	private String transactionReferenceId;
	
	/**
	 * Gets the certificate start date.
	 *
	 * @return the certificate start date
	 */
	public Date getCertificateStartDate() {
		return certificateStartDate;
	}

	/**
	 * Sets the certificate start date.
	 *
	 * @param certificateStartDate the new certificate start date
	 */
	public void setCertificateStartDate(Date certificateStartDate) {
		this.certificateStartDate = certificateStartDate;
	}

	/**
	 * Gets the certificate end date.
	 *
	 * @return the certificate end date
	 */
	public Date getCertificateEndDate() {
		return certificateEndDate;
	}

	/**
	 * Sets the certificate end date.
	 *
	 * @param certificateEndDate the new certificate end date
	 */
	public void setCertificateEndDate(Date certificateEndDate) {
		this.certificateEndDate = certificateEndDate;
	}

	/**
	 * Gets the pki key id.
	 *
	 * @return the pki key id
	 */
	public String getPkiKeyId() {
		return pkiKeyId;
	}

	/**
	 * Sets the pki key id.
	 *
	 * @param pkiKeyId the new pki key id
	 */
	public void setPkiKeyId(String pkiKeyId) {
		this.pkiKeyId = pkiKeyId;
	}

	/**
	 * Gets the certificate data.
	 *
	 * @return the certificate data
	 */
	public String getCertificateData() {
		return certificateData;
	}

	/**
	 * Sets the certificate data.
	 *
	 * @param certificateData the new certificate data
	 */
	public void setCertificateData(String certificateData) {
		this.certificateData = certificateData;
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
	 * @param "organizationId the new organization id
	 */
	
	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
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

	public String getWrappedKey() {
		return wrappedKey;
	}

	public void setWrappedKey(String wrappedKey) {
		this.wrappedKey = wrappedKey;
	}

	/**
	 * Gets the revocation reason.
	 *
	 * @return the revocation reason
	 */
	public String getRevocationReason() {
		return revocationReason;
	}

	/**
	 * Sets the revocation reason.
	 *
	 * @param revocationReason the new revocation reason
	 */
	public void setRevocationReason(String revocationReason) {
		this.revocationReason = revocationReason;
	}

	/**
	 * Gets the updated date.
	 *
	 * @return the updated date
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate the new updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	
	public String getTransactionReferenceId() {
		return transactionReferenceId;
	}

	public void setTransactionReferenceId(String transactionReferenceId) {
		this.transactionReferenceId = transactionReferenceId;
	}

	@Override
	public String toString() {
		return "WalletSignCertificate [certificateSerialNumber=" + certificateSerialNumber + ", organizationUid="
				+ organizationUid + ", pkiKeyId=" + pkiKeyId + ", certificateData=" + certificateData + ", wrappedKey="
				+ wrappedKey + ", certificateStartDate=" + certificateStartDate + ", certificateEndDate="
				+ certificateEndDate + ", certificateStatus=" + certificateStatus + ", revocationReason="
				+ revocationReason + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate
				+ ", certificateType=" + certificateType + ", transactionReferenceId=" + transactionReferenceId + "]";
	}

}
