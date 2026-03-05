package com.dtt.organization.dto;

import java.io.Serializable;

public class OrgSigntoryDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fullName;
	private String suid;
	private String email;
	private String thumbNailUri;

	private String organizationEmail;
	private boolean hasEseal;


	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSuid() {
		return suid;
	}

	public void setSuid(String suid) {
		this.suid = suid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getThumbNailUri() {
		return thumbNailUri;
	}

	public void setThumbNailUri(String thumbNailUri) {
		this.thumbNailUri = thumbNailUri;
	}
	

//

	public String getOrganizationEmail() {
		return organizationEmail;
	}

	public void setOrganizationEmail(String organizationEmail) {
		this.organizationEmail = organizationEmail;
	}

	public boolean isHasEseal() {
		return hasEseal;
	}

	public void setHasEseal(boolean hasEseal) {
		this.hasEseal = hasEseal;
	}

	@Override
	public String toString() {
		return "OrgSigntoryDto{" +
				"fullName='" + fullName + '\'' +
				", suid='" + suid + '\'' +
				", email='" + email + '\'' +
				", thumbNailUri='" + thumbNailUri + '\'' +
				", organizationEmail='" + organizationEmail + '\'' +
				", hasEseal=" + hasEseal +
				'}';
	}
}
