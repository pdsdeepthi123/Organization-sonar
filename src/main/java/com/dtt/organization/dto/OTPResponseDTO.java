package com.dtt.organization.dto;

public class OTPResponseDTO {

	private String mobileOTP;
	private String emailOTP;
	private String mobileEncrptyOTP;
	private String emailEncrptyOTP;
	
	
	private Integer ttl;

	public String getMobileOTP() {
		return mobileOTP;
	}

	public void setMobileOTP(String mobileOTP) {
		this.mobileOTP = mobileOTP;
	}

	public String getEmailOTP() {
		return emailOTP;
	}

	public void setEmailOTP(String emailOTP) {
		this.emailOTP = emailOTP;
	}

	public String getMobileEncrptyOTP() {
		return mobileEncrptyOTP;
	}

	public void setMobileEncrptyOTP(String mobileEncrptyOTP) {
		this.mobileEncrptyOTP = mobileEncrptyOTP;
	}

	public String getEmailEncrptyOTP() {
		return emailEncrptyOTP;
	}

	public void setEmailEncrptyOTP(String emailEncrptyOTP) {
		this.emailEncrptyOTP = emailEncrptyOTP;
	}

	public Integer getTtl() {
		return ttl;
	}

	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}

	@Override
	public String toString() {
		return "OTPResponseDTO [mobileOTP=" + mobileOTP + ", emailOTP=" + emailOTP + ", mobileEncrptyOTP="
				+ mobileEncrptyOTP + ", emailEncrptyOTP=" + emailEncrptyOTP + ", ttl=" + ttl + "]";
	}		
}
