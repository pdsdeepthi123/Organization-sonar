package com.dtt.organization.dto;

import java.io.Serializable;

public class PrepteryStatusResponseDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private String organizationName;
	
	private String organizationUid;
	
	private String subscriberEmailList;
	
	private boolean signatory;
	
	private boolean eSealSignatory;
	
	private boolean eSealPrepatory;
	
	private boolean template;
	
	private boolean bulksign;

	private boolean delegate;
	
	private boolean lsaPrivilege;
	
	private boolean digitalFormPrivilege;



	public boolean isDelegate() {
		return delegate;
	}

	public void setDelegate(boolean delegate) {
		this.delegate = delegate;
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

	public String getSubscriberEmailList() {
		return subscriberEmailList;
	}

	public void setSubscriberEmailList(String subscriberEmailList) {
		this.subscriberEmailList = subscriberEmailList;
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
	
	public boolean isLsaPrivilege() {
		return lsaPrivilege;
	}

	public void setLsaPrivilege(boolean lsaPrivilege) {
		this.lsaPrivilege = lsaPrivilege;
	}

	public boolean isDigitalFormPrivilege() {
		return digitalFormPrivilege;
	}

	public void setDigitalFormPrivilege(boolean digitalFormPrivilege) {
		this.digitalFormPrivilege = digitalFormPrivilege;
	}

	@Override
	public String toString() {
		return "PrepteryStatusResponseDTO{" +
				"organizationName='" + organizationName + '\'' +
				", organizationUid='" + organizationUid + '\'' +
				", subscriberEmailList='" + subscriberEmailList + '\'' +
				", signatory=" + signatory +
				", eSealSignatory=" + eSealSignatory +
				", eSealPrepatory=" + eSealPrepatory +
				", template=" + template +
				", bulksign=" + bulksign +
				", delegate=" + delegate +
				", lsaPrivilege=" + lsaPrivilege +
				", digitalFormPrivilege=" + digitalFormPrivilege +
				'}';
	}
}
