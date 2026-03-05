package com.dtt.organization.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;

/**
 * The persistent class for the subscriber_complete_details database table.
 * 
 */
@Entity
@Table(name = "subscriber_complete_details")
@NamedQuery(name = "SubscriberCompleteDetail.findAll", query = "SELECT s FROM SubscriberCompleteDetail s")
public class SubscriberCompleteDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cerificate_expiry_date")
	private Date cerificateExpiryDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "certificate_issue_date")
	private Date certificateIssueDate;

	@Column(name = "certificate_status")
	private String certificateStatus;

	@Column(name = "certificate_serial_number")
	private String certificateSerialNumber;

	@Column(name = "full_name")
	private String fullName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "device_status")
	private String deviceStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "device_registration_time")
	private Date deviceRegistrationTimne;

	@Column(name = "id_doc_number")
	private String idDocNumber;

	@Column(name = "id_doc_type")
	private String idDocType;

	@Column(name = "subscriber_status")
	private String subscriberStatus;

	@Id
	@Column(name = "subscriber_uid")
	private String subscriberUid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sign_pin_set_date")
	private Date signPinSetDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "auth_pin_set_date")
	private Date authPinSetDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "on_boarding_time")
	private Date onBoardingTime;

	@Column(name = "selfie_uri")
	private String selfieUri;
	
	@Column(name = "selfie_thumbnail_uri")
	private String selfieThumbnailUri;

	@Column(name = "on_boarding_method")
	private String onBoardingMethod;

	@Column(name = "level_of_assurance")
	private String levelOfAssurance;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "geo_location")
	private String geoLocation;

	@Column(name = "gender")
	private String gender;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "revocation_date")
	private Date revocationDate;

	@Column(name = "revocation_reason")
	private String revocationReason;

	@Column(name = "video_url")
	private String videoUrl;

	@Column(name = "video_type")
	private String videoType;


	public Date getCerificateExpiryDate() {
		return cerificateExpiryDate;
	}

	public void setCerificateExpiryDate(Date cerificateExpiryDate) {
		this.cerificateExpiryDate = cerificateExpiryDate;
	}

	public Date getCertificateIssueDate() {
		return certificateIssueDate;
	}

	public void setCertificateIssueDate(Date certificateIssueDate) {
		this.certificateIssueDate = certificateIssueDate;
	}

	public String getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public String getCertificateSerialNumber() {
		return certificateSerialNumber;
	}

	public void setCertificateSerialNumber(String certificateSerialNumber) {
		this.certificateSerialNumber = certificateSerialNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public Date getDeviceRegistrationTimne() {
		return deviceRegistrationTimne;
	}

	public void setDeviceRegistrationTimne(Date deviceRegistrationTimne) {
		this.deviceRegistrationTimne = deviceRegistrationTimne;
	}

	public String getIdDocNumber() {
		return idDocNumber;
	}

	public void setIdDocNumber(String idDocNumber) {
		this.idDocNumber = idDocNumber;
	}

	public String getIdDocType() {
		return idDocType;
	}

	public void setIdDocType(String idDocType) {
		this.idDocType = idDocType;
	}

	public String getSubscriberStatus() {
		return subscriberStatus;
	}

	public void setSubscriberStatus(String subscriberStatus) {
		this.subscriberStatus = subscriberStatus;
	}

	public String getSubscriberUid() {
		return subscriberUid;
	}

	public void setSubscriberUid(String subscriberUid) {
		this.subscriberUid = subscriberUid;
	}

	public Date getSignPinSetDate() {
		return signPinSetDate;
	}

	public void setSignPinSetDate(Date signPinSetDate) {
		this.signPinSetDate = signPinSetDate;
	}

	public Date getAuthPinSetDate() {
		return authPinSetDate;
	}

	public void setAuthPinSetDate(Date authPinSetDate) {
		this.authPinSetDate = authPinSetDate;
	}

	public Date getOnBoardingTime() {
		return onBoardingTime;
	}

	public void setOnBoardingTime(Date onBoardingTime) {
		this.onBoardingTime = onBoardingTime;
	}

	public String getSelfieUri() {
		return selfieUri;
	}

	public void setSelfieUri(String selfieUri) {
		this.selfieUri = selfieUri;
	}

	public String getSelfieThumbnailUri() {
		return selfieThumbnailUri;
	}

	public void setSelfieThumbnailUri(String selfieThumbnailUri) {
		this.selfieThumbnailUri = selfieThumbnailUri;
	}

	public String getOnBoardingMethod() {
		return onBoardingMethod;
	}

	public void setOnBoardingMethod(String onBoardingMethod) {
		this.onBoardingMethod = onBoardingMethod;
	}

	public String getLevelOfAssurance() {
		return levelOfAssurance;
	}

	public void setLevelOfAssurance(String levelOfAssurance) {
		this.levelOfAssurance = levelOfAssurance;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getRevocationDate() {
		return revocationDate;
	}

	public void setRevocationDate(Date revocationDate) {
		this.revocationDate = revocationDate;
	}

	public String getRevocationReason() {
		return revocationReason;
	}

	public void setRevocationReason(String revocationReason) {
		this.revocationReason = revocationReason;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	

}