package com.dtt.organization.dto;

public class RARequestDTO {


	private String geoLocation;


	private String subscriberUniqueId;
	
	
	private String organizationUid;
	
	
	private String organozationStatus;


	private String reasonId;


	private String subscriberStatus;


	private String description;


	private String searchType;

	private String searchValue;
	

	public String getReasonId() {
		return reasonId;
	}


	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	public String getSubscriberStatus() {
		return subscriberStatus;
	}


	public void setSubscriberStatus(String subscriberStatus) {
		this.subscriberStatus = subscriberStatus;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}


	public String getSubscriberUniqueId() {
		return subscriberUniqueId;
	}


	public void setSubscriberUniqueId(String subscriberUniqueId) {
		this.subscriberUniqueId = subscriberUniqueId;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	

	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	public String getOrganozationStatus() {
		return organozationStatus;
	}

	public void setOrganozationStatus(String organozationStatus) {
		this.organozationStatus = organozationStatus;
	}

	@Override
	public String toString() {
		return "RARequestDTO [geoLocation=" + geoLocation + ", subscriberUniqueId=" + subscriberUniqueId
				+ ", organizationUid=" + organizationUid + ", organozationStatus=" + organozationStatus + ", reasonId="
				+ reasonId + ", subscriberStatus=" + subscriberStatus + ", description=" + description + ", searchType="
				+ searchType + ", searchValue=" + searchValue + "]";
	}


	
	

}
