package com.dtt.organization.dto;

import java.io.Serializable;

public class PostpaidReqest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subscriberSuid;
	
	private String organizationId;
	
	private Boolean transactionForOrganization;
	
	private String serviceName;

	public String getSubscriberSuid() {
		return subscriberSuid;
	}

	public void setSubscriberSuid(String subscriberSuid) {
		this.subscriberSuid = subscriberSuid;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getTransactionForOrganization() {
		return transactionForOrganization;
	}

	public void setTransactionForOrganization(Boolean transactionForOrganization) {
		this.transactionForOrganization = transactionForOrganization;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "PostpaidReqest [subscriberSuid=" + subscriberSuid + ", organizationId=" + organizationId
				+ ", transactionForOrganization=" + transactionForOrganization + ", serviceName=" + serviceName + "]";
	}
	

}
