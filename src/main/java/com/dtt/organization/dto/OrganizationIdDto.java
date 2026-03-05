package com.dtt.organization.dto;

import java.io.Serializable;
import java.util.List;

public class OrganizationIdDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> orgId;

	public List<String> getOrgId() {
		return orgId;
	}

	public void setOrgId(List<String> orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return "OrganizationIdDto [orgId=" + orgId + "]";
	}
}
