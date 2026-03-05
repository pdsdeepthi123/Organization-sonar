package com.dtt.organization.dto;

import java.io.Serializable;

public class TrustedStakeholderDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String spocUgpassEmail;
	private String referenceId;
	private String organizationUid;
	private String referredBy;
	private boolean status;
	
	private String onboardingTime;
	
	private String creationTime;
	
	private String stakeholderType;
	
		
	public String getReferredBy() {
		return referredBy;
	}

	public void setReferredBy(String referredBy) {
		this.referredBy = referredBy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpocUgpassEmail() {
		return spocUgpassEmail;
	}

	public void setSpocUgpassEmail(String spocUgpassEmail) {
		this.spocUgpassEmail = spocUgpassEmail;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}	
	
	public String getOnboardingTime() {
		return onboardingTime;
	}

	public void setOnboardingTime(String onboardingTime) {
		this.onboardingTime = onboardingTime;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getStakeholderType() {
		return stakeholderType;
	}

	public void setStakeholderType(String stakeholderType) {
		this.stakeholderType = stakeholderType;
	}
	@Override
	public String toString() {
		return "{" + "\"id\":\"" + id + "\"," + "\"name\":\"" + name + "\","
				+ "\"spocUgpassEmail\":\"" + spocUgpassEmail + "\"," + "\"referenceId\":\"" + referenceId + "\","
				+ "\"organizationUid\":\"" + organizationUid + "\"," + "\"status\":\"" + status + "\"," + "\"onboardingTime\":\""
				+ onboardingTime + "\"," + "\"creationTime\":\"" + creationTime + "\"," + "\"referredBy\":\"" + referredBy +
				"\","+"\"stakeholderType\":" + stakeholderType +"\""
				+ "}";
	}


	
}
