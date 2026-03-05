package com.dtt.organization.dto;

import java.io.Serializable;

public class OrganizationListResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String organizationName;

	private String organizationUid;

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

	@Override
	public String toString() {
		return "OrganizationListResponseDTO [organizationName=" + organizationName + ", organizationUid="
				+ organizationUid + "]";
	}

}
