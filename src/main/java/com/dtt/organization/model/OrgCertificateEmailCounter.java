package com.dtt.organization.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "org_certificate_email_counter")
public class OrgCertificateEmailCounter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "organization_uid")
	private String organizationUid;
	@Column(name = "organization_name")
	private String organizationName;
	@Column(name = "spoc_ugpass_email")
	private String spocUgpassEmail;
	@Column(name = "counter_15_day")
	private String counter15;
	@Column(name = "counter_10_day")
	private String counter10;
	@Column(name = "counter_5_day")
	private String counter5;
	@Column(name = "counter_0_day")
	private String counter;
	public String getOrganizationUid() {
		return organizationUid;
	}
	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getSpocUgpassEmail() {
		return spocUgpassEmail;
	}
	public void setSpocUgpassEmail(String spocUgpassEmail) {
		this.spocUgpassEmail = spocUgpassEmail;
	}
	public String getCounter15() {
		return counter15;
	}
	public void setCounter15(String counter15) {
		this.counter15 = counter15;
	}
	public String getCounter10() {
		return counter10;
	}
	public void setCounter10(String counter10) {
		this.counter10 = counter10;
	}
	public String getCounter5() {
		return counter5;
	}
	public void setCounter5(String counter5) {
		this.counter5 = counter5;
	}
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
	}
	@Override
	public String toString() {
		return "OrgCertificateEmailCounter [organizationUid=" + organizationUid + ", organizationName="
				+ organizationName + ", spocUgpassEmail=" + spocUgpassEmail + ", counter15=" + counter15
				+ ", counter10=" + counter10 + ", counter5=" + counter5 + ", counter=" + counter + "]";
	}
}
