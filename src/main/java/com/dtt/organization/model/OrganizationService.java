package com.dtt.organization.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "organization_services")
@NamedQuery(name="OrganizationService.findAll", query="SELECT o FROM OrganizationService o")
public class OrganizationService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "organization_service_id", nullable = false, unique = true)
	private int organizationServiceId;

	
	@Column(name="organization_uid")
	private String organizationUid;
	
	@Column(name="service_name")
	private String serviceList;

	public int getOrganizationServiceId() {
		return organizationServiceId;
	}

	public void setOrganizationServiceId(int organizationServiceId) {
		this.organizationServiceId = organizationServiceId;
	}

	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	public String getServiceList() {
		return serviceList;
	}

	public void setServiceList(String serviceList) {
		this.serviceList = serviceList;
	}

	@Override
	public String toString() {
		return "OrganizationService [organizationServiceId=" + organizationServiceId + ", organizationUid="
				+ organizationUid + ", serviceList=" + serviceList + "]";
	}

		
}
