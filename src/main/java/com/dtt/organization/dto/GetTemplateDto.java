package com.dtt.organization.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public class GetTemplateDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Organization Uid Cannot be null or empty")
	private String orgId;
	
	//@NotBlank(message = "Subscriber Uid Cannot be null or empty")
	private String suid;
	
	private String orgName;

	@NotBlank(message = "Email cannot be null or empty")
	private String email;
	
	private int templateId;

	private boolean checkEseal;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSuid() {
		return suid;
	}

	public void setSuid(String suid) {
		this.suid = suid;
	}

	public String getOrgName() {
		return orgName;
	}



	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}


	public boolean isCheckEseal() {
		return checkEseal;
	}

	public void setCheckEseal(boolean checkEseal) {
		this.checkEseal = checkEseal;
	}

	@Override
	public String toString() {
		return "GetTemplateDto{" +
				"orgId='" + orgId + '\'' +
				", suid='" + suid + '\'' +
				", orgName='" + orgName + '\'' +
				", email='" + email + '\'' +
				", templateId=" + templateId +
				", checkEseal=" + checkEseal +
				'}';
	}
}
