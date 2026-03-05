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
@Table(name = "subscribers")
@NamedQuery(name = "Subscriber.findAll", query = "SELECT s FROM Subscriber s")
public class Subscriber implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "id_doc_number")
	private String idDocNumber;

	@Column(name = "id_doc_type")
	private String idDocType;

	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Id
	@Column(name = "subscriber_uid")
	private String subscriberUid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name="national_id")
	private String nationalId;

	@Column(name="title")
	private String title;
	


	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getSubscriberUid() {
		return this.subscriberUid;
	}

	public void setSubscriberUid(String subscriberUid) {
		this.subscriberUid = subscriberUid;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Subscriber [createdDate=" + createdDate + ", dateOfBirth=" + dateOfBirth + ", emailId=" + emailId
				+ ", fullName=" + fullName + ", idDocNumber=" + idDocNumber + ", idDocType=" + idDocType
				+ ", mobileNumber=" + mobileNumber + ", subscriberUid=" + subscriberUid + ", updatedDate=" + updatedDate
				+ ", nationalId=" + nationalId + ", title=" + title + "]";
	}
	
}
