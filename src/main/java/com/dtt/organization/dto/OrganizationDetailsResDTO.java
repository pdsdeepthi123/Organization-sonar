package com.dtt.organization.dto;

import java.io.Serializable;

public class OrganizationDetailsResDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int orgContactsEmailId;

	private String employeeEmail;
	
	private String organizationName;
	
	private String organizationUid;
	
	private boolean ugpassLinkApproved;

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

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	public boolean isUgpassLinkApproved() {
		return ugpassLinkApproved;
	}

	public void setUgpassLinkApproved(boolean ugpassLinkApproved) {
		this.ugpassLinkApproved = ugpassLinkApproved;
	}

	@Override
	public String toString() {
		return "OrganizationDetailsResDTO{" +
				"orgContactsEmailId='" + orgContactsEmailId + '\'' +
				", employeeEmail='" + employeeEmail + '\'' +
				", organizationName='" + organizationName + '\'' +
				", organizationUid='" + organizationUid + '\'' +
				", ugpassLinkApproved=" + ugpassLinkApproved +
				'}';
	}
}
