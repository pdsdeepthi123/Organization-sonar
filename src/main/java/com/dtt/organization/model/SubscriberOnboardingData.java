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
@Table(name="subscriber_onboarding_data")
@NamedQuery(name="SubscriberOnboardingData.findAll", query="SELECT s FROM SubscriberOnboardingData s")
public class SubscriberOnboardingData implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Column(name="created_date")
	private String createdDate;

	@Column(name="documents_location")
	private String documentsLocation;

	@Column(name="id_doc_number")
	private String idDocNumber;

	@Column(name="id_doc_type")
	private String idDocType;

	@Column(name="id_doc_uri")
	private String idDocUri;

	@Column(name="level_of_assurance")
	private String levelOfAssurance;

	@Column(name="onboarding_data_fields_json")
	private String onboardingDataFieldsJson;

	@Column(name="onboarding_method")
	private String onboardingMethod;

	@Column(name="prev_id_doc_number")
	private String prevIdDocNumber;

	@Column(name="selfie_uri")
	private String selfieUri;
	
	@Column(name="selfie_thumbnail_uri")
	private String selfieThumbnailUri;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="subscriber_onboarding_data_id")
	private int subscriberOnboardingDataId;

	@Column(name="subscriber_type")
	private String subscriberType;

	@Column(name="subscriber_uid")
	private String subscriberUid;

	@Column(name="template_id")
	private int templateId;

	@Column(name="id_doc_code")
	private String idDocCode;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="geolocation")
	private String geolocation;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name = "optional_data1")
	private String optionalData1;
	
	@Column(name = "date_of_expiry")
	private String dateOfExpiry;
	


	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getDocumentsLocation() {
		return this.documentsLocation;
	}

	public void setDocumentsLocation(String documentsLocation) {
		this.documentsLocation = documentsLocation;
	}

	public String getIdDocNumber() {
		return this.idDocNumber;
	}

	public void setIdDocNumber(String idDocNumber) {
		this.idDocNumber = idDocNumber;
	}

	public String getIdDocType() {
		return this.idDocType;
	}

	public void setIdDocType(String idDocType) {
		this.idDocType = idDocType;
	}

	public String getIdDocUri() {
		return this.idDocUri;
	}

	public void setIdDocUri(String idDocUri) {
		this.idDocUri = idDocUri;
	}

	public String getLevelOfAssurance() {
		return this.levelOfAssurance;
	}

	public void setLevelOfAssurance(String levelOfAssurance) {
		this.levelOfAssurance = levelOfAssurance;
	}

	public String getOnboardingDataFieldsJson() {
		return this.onboardingDataFieldsJson;
	}

	public void setOnboardingDataFieldsJson(String onboardingDataFieldsJson) {
		this.onboardingDataFieldsJson = onboardingDataFieldsJson;
	}

	public String getOnboardingMethod() {
		return this.onboardingMethod;
	}

	public void setOnboardingMethod(String onboardingMethod) {
		this.onboardingMethod = onboardingMethod;
	}

	public String getPrevIdDocNumber() {
		return this.prevIdDocNumber;
	}

	public void setPrevIdDocNumber(String prevIdDocNumber) {
		this.prevIdDocNumber = prevIdDocNumber;
	}

	public String getSelfieUri() {
		return this.selfieUri;
	}

	public void setSelfieUri(String selfieUri) {
		this.selfieUri = selfieUri;
	}

	public int getSubscriberOnboardingDataId() {
		return this.subscriberOnboardingDataId;
	}

	public void setSubscriberOnboardingDataId(int subscriberOnboardingDataId) {
		this.subscriberOnboardingDataId = subscriberOnboardingDataId;
	}

	public String getSubscriberType() {
		return this.subscriberType;
	}

	public void setSubscriberType(String subscriberType) {
		this.subscriberType = subscriberType;
	}

	public String getSubscriberUid() {
		return this.subscriberUid;
	}

	public void setSubscriberUid(String subscriberUid) {
		this.subscriberUid = subscriberUid;
	}

	public int getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getIdDocCode() {
		return idDocCode;
	}

	public void setIdDocCode(String idDocCode) {
		this.idDocCode = idDocCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	public String getSelfieThumbnailUri() {
		return selfieThumbnailUri;
	}

	public void setSelfieThumbnailUri(String selfieThumbnailUri) {
		this.selfieThumbnailUri = selfieThumbnailUri;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOptionalData1() {
		return optionalData1;
	}

	public void setOptionalData1(String optionalData1) {
		this.optionalData1 = optionalData1;
	}

	public String getDateOfExpiry() {
		return dateOfExpiry;
	}

	public void setDateOfExpiry(String dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
	}

	@Override
	public String toString() {
		return "SubscriberOnboardingData [createdDate=" + createdDate + ", documentsLocation=" + documentsLocation
				+ ", idDocNumber=" + idDocNumber + ", idDocType=" + idDocType + ", idDocUri=" + idDocUri
				+ ", levelOfAssurance=" + levelOfAssurance + ", onboardingDataFieldsJson=" + onboardingDataFieldsJson
				+ ", onboardingMethod=" + onboardingMethod + ", prevIdDocNumber=" + prevIdDocNumber + ", selfieUri="
				+ selfieUri + ", selfieThumbnailUri=" + selfieThumbnailUri + ", subscriberOnboardingDataId="
				+ subscriberOnboardingDataId + ", subscriberType=" + subscriberType + ", subscriberUid=" + subscriberUid
				+ ", templateId=" + templateId + ", idDocCode=" + idDocCode + ", gender=" + gender + ", geolocation="
				+ geolocation + ", remarks=" + remarks + ", optionalData1=" + optionalData1 + ", dateOfExpiry="
				+ dateOfExpiry + "]";
	}
	
	
}
