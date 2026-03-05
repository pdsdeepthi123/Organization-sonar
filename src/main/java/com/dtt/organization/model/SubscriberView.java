package com.dtt.organization.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "subscriber_view")
@NamedQuery(name = "SubscriberView.findAll", query = "SELECT s FROM SubscriberView s")
public class SubscriberView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "subscriber_uid")
	private String subscriberUid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@Column(name = "id_doc_number")
	private String idDocNumber;

	@Column(name = "id_doc_type")
	private String idDocType;

	@Column(name = "display_name")
	private String displayName;
	
	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "email")
	private String emailId;
	
	@Column(name = "org_emails_list")
	private String orgEmailsList;
	
	@Column(name = "certificate_status")
	private String certificateStatus;
	
	@Column(name = "subscriber_status")
	private String subscriberStatus;
	
	@Column(name = "fcm_token")
	private String fcmToken;
	
	@Column(name = "loa")
	private String levlOfAssurance;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "country")
	private String country;

	public String getSubscriberUid() {
		return subscriberUid;
	}

	public void setSubscriberUid(String subscriberUid) {
		this.subscriberUid = subscriberUid;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public String getOrgEmailsList() {
		return orgEmailsList;
	}

	public void setOrgEmailsList(String orgEmailsList) {
		this.orgEmailsList = orgEmailsList;
	}

	public String getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public String getSubscriberStatus() {
		return subscriberStatus;
	}

	public void setSubscriberStatus(String subscriberStatus) {
		this.subscriberStatus = subscriberStatus;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public String getLevlOfAssurance() {
		return levlOfAssurance;
	}

	public void setLevlOfAssurance(String levlOfAssurance) {
		this.levlOfAssurance = levlOfAssurance;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "SubscriberView [subscriberUid=" + subscriberUid + ", dateOfBirth=" + dateOfBirth + ", idDocNumber="
				+ idDocNumber + ", idDocType=" + idDocType + ", displayName=" + displayName + ", mobileNumber="
				+ mobileNumber + ", emailId=" + emailId + ", orgEmailsList=" + orgEmailsList + ", certificateStatus="
				+ certificateStatus + ", subscriberStatus=" + subscriberStatus + ", fcmToken=" + fcmToken
				+ ", levlOfAssurance=" + levlOfAssurance + ", gender=" + gender + ", country=" + country + "]";
	}

}
