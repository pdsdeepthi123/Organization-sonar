package com.dtt.organization.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "org_subscriber_email")
@NamedQuery(name = "OrgContactsEmail.findAll", query = "SELECT o FROM OrgContactsEmail o")
public class OrgContactsEmail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "org_contacts_id")
	private int orgContactsEmailId;

	@Column(name = "organization_uid")
	private String organizationUid;

	@Column(name = "employee_email")
	private String employeeEmail;

	@Column(name = "is_org_signatory")
	private boolean signatory;
	@Column(name = "is_delegate")
	private boolean delegate;

	@Column(name = "is_eseal_signatory")
	private boolean eSealSignatory;

	@Column(name = "is_eseal_preparatory")
	private boolean eSealPreparatory;

	@Column(name = "is_template")
	private boolean template;

	@Column(name = "is_bulk_sign")
	private boolean bulksign;

	@Column(name = "designation")
	private String designation;

	@Column(name = "signature_photo")
	private String signaturePhoto;


	@Column(name = "ugpass_email")
	private String ugpassEmail;

	@Column(name = "passport_number")
	private String passportNumber;

	@Column(name = "national_id_number")
	private String nationalIdNumber;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "ugpass_user_link_approved")
	private boolean ugpassUserLinkApproved;

	@Column(name = "subscriber_uid")
	private String subscriberUid;

	@Column(name = "status")
	private String status;


	@Column(name = "short_signature")
	private String initial;
	
	@Column(name = "is_digital_form")
	private boolean digitalFormPrivilege;

	public boolean isDelegate() {
		return delegate;
	}

	public void setDelegate(boolean delegate) {
		this.delegate = delegate;
	}

	public int getOrgContactsEmailId() {
		return orgContactsEmailId;
	}

	public void setOrgContactsEmailId(int orgContactsEmailId) {
		this.orgContactsEmailId = orgContactsEmailId;
	}

	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public boolean isSignatory() {
		return signatory;
	}

	public void setSignatory(boolean signatory) {
		this.signatory = signatory;
	}

	public boolean iseSealSignatory() {
		return eSealSignatory;
	}

	public void seteSealSignatory(boolean eSealSignatory) {
		this.eSealSignatory = eSealSignatory;
	}

	public boolean iseSealPreparatory() {
		return eSealPreparatory;
	}

	public void seteSealPreparatory(boolean eSealPreparatory) {
		this.eSealPreparatory = eSealPreparatory;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

	public boolean isBulksign() {
		return bulksign;
	}

	public void setBulksign(boolean bulksign) {
		this.bulksign = bulksign;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getSignaturePhoto() {
		return signaturePhoto;
	}

	public void setSignaturePhoto(String signaturePhoto) {
		this.signaturePhoto = signaturePhoto;
	}

	public String getUgpassEmail() {
		return ugpassEmail;
	}

	public void setUgpassEmail(String ugpassEmail) {
		this.ugpassEmail = ugpassEmail;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getNationalIdNumber() {
		return nationalIdNumber;
	}

	public void setNationalIdNumber(String nationalIdNumber) {
		this.nationalIdNumber = nationalIdNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public boolean isUgpassUserLinkApproved() {
		return ugpassUserLinkApproved;
	}

	public void setUgpassUserLinkApproved(boolean ugpassUserLinkApproved) {
		this.ugpassUserLinkApproved = ugpassUserLinkApproved;
	}

	public String getSubscriberUid() {
		return subscriberUid;
	}

	public void setSubscriberUid(String subscriberUid) {
		this.subscriberUid = subscriberUid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	



	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}
	
	public boolean isDigitalFormPrivilege() {
		return digitalFormPrivilege;
	}

	public void setDigitalFormPrivilege(boolean digitalFormPrivilege) {
		this.digitalFormPrivilege = digitalFormPrivilege;
	}

	@Override
	public String toString() {
		return "OrgContactsEmail{" +
				"orgContactsEmailId=" + orgContactsEmailId +
				", organizationUid='" + organizationUid + '\'' +
				", employeeEmail='" + employeeEmail + '\'' +
				", signatory=" + signatory +
				", delegate=" + delegate +
				", eSealSignatory=" + eSealSignatory +
				", eSealPreparatory=" + eSealPreparatory +
				", template=" + template +
				", bulksign=" + bulksign +
				", designation='" + designation + '\'' +
				", signaturePhoto='" + signaturePhoto + '\'' +
				", ugpassEmail='" + ugpassEmail + '\'' +
				", passportNumber='" + passportNumber + '\'' +
				", nationalIdNumber='" + nationalIdNumber + '\'' +
				", mobileNumber='" + mobileNumber + '\'' +
				", ugpassUserLinkApproved=" + ugpassUserLinkApproved +
				", subscriberUid='" + subscriberUid + '\'' +
				", status='" + status + '\'' +
				", initial='" + initial + '\'' +
				", digitalFormPrivilege='" + digitalFormPrivilege + '\'' +
				'}';
	}

	
}
