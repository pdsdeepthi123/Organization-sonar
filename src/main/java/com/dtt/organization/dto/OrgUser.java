package com.dtt.organization.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int orgContactsEmailId;

	@NotBlank(message = "Please provide the organization email address.")
	@Email(message = "Please enter a valid email address for the organization.")
	private String employeeEmail;

	private boolean signatory ;

	private boolean eSealSignatory;

	private boolean eSealPrepatory;
	
	private boolean template;
	
	private boolean bulksign;

	private boolean delegate;

	private String designation;
	
	private String signaturePhoto;

	private String ugpassEmail;


	private String passportNumber;


	private String nationalIdNumber;


	private String mobileNumber;


	private boolean ugpassUserLinkApproved;


	private String subscriberUid;


	private String status;
	
	private boolean lsaPrivilege;
	
	private boolean digitalFormPrivilege;


	private String organizationUid;

	private String initial;

	public int getOrgContactsEmailId() {
		return orgContactsEmailId;
	}

	public void setOrgContactsEmailId(int orgContactsEmailId) {
		this.orgContactsEmailId = orgContactsEmailId;
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

	public boolean iseSealPrepatory() {
		return eSealPrepatory;
	}

	public void seteSealPrepatory(boolean eSealPrepatory) {
		this.eSealPrepatory = eSealPrepatory;
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

	public boolean isDelegate() {
		return delegate;
	}

	public void setDelegate(boolean delegate) {
		this.delegate = delegate;
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

	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}
	
	public boolean isLsaPrivilege() {
		return lsaPrivilege;
	}

	public void setLsaPrivilege(boolean lsaPrivilege) {
		this.lsaPrivilege = lsaPrivilege;
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
		return "OrgUser{" +
				"orgContactsEmailId=" + orgContactsEmailId +
				", employeeEmail='" + employeeEmail + '\'' +
				", signatory=" + signatory +
				", eSealSignatory=" + eSealSignatory +
				", eSealPrepatory=" + eSealPrepatory +
				", template=" + template +
				", bulksign=" + bulksign +
				", delegate=" + delegate +
				", designation='" + designation + '\'' +
				", signaturePhoto='" + signaturePhoto + '\'' +
				", ugpassEmail='" + ugpassEmail + '\'' +
				", passportNumber='" + passportNumber + '\'' +
				", nationalIdNumber='" + nationalIdNumber + '\'' +
				", mobileNumber='" + mobileNumber + '\'' +
				", ugpassUserLinkApproved=" + ugpassUserLinkApproved +
				", subscriberUid='" + subscriberUid + '\'' +
				", status='" + status + '\'' +
				", lsaPrivilege=" + lsaPrivilege +
				", organizationUid='" + organizationUid + '\'' +
				", initial='" + initial + '\'' +
				", digitalFormPrivilege='" + digitalFormPrivilege + '\'' +
				'}';
	}
}
