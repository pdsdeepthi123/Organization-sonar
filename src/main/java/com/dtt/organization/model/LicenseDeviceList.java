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
@Table(name="license_device_list")
@NamedQuery(name="LicenseDeviceList.findAll", query="SELECT s FROM LicenseDeviceList s")
public class LicenseDeviceList implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "client_id")
	private String clientId;
	
	@Column(name = "organization_name")
	private String organizationName;
	
	@Column(name = "application_name")
	private String applicationName;
	
	@Column(name = "device_id")
	private String deviceId;
	
	@Column(name = "created_date")
	private String createdDate;
	
	@Column(name = "updated_date")
	private String updatedDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@Override
	public String toString() {
		return "LicenseDeviceList [id=" + id + ", clientId=" + clientId + ", organizationName=" + organizationName
				+ ", applicationName=" + applicationName + ", deviceId=" + deviceId + ", createdDate=" + createdDate
				+ ", updatedDate=" + updatedDate + "]";
	}
	

	
}
