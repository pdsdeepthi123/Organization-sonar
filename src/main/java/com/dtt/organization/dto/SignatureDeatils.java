package com.dtt.organization.dto;

import java.io.Serializable;

public class SignatureDeatils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String signedBy;

	private String signedTime;

	private String validationTime;

	private boolean signatureValid;

	private String certificateSerialNumber;

	public String getCertificateSerialNumber() {
		return certificateSerialNumber;
	}

	public void setCertificateSerialNumber(String certificateSerialNumber) {
		this.certificateSerialNumber = certificateSerialNumber;
	}

	public String getSignedBy() {
		return signedBy;
	}

	public void setSignedBy(String signedBy) {
		this.signedBy = signedBy;
	}

	public String getSignedTime() {
		return signedTime;
	}

	public void setSignedTime(String signedTime) {
		this.signedTime = signedTime;
	}

	public String getValidationTime() {
		return validationTime;
	}

	public void setValidationTime(String validationTime) {
		this.validationTime = validationTime;
	}

	public boolean isSignatureValid() {
		return signatureValid;
	}

	public void setSignatureValid(boolean signatureValid) {
		this.signatureValid = signatureValid;
	}

	@Override
	public String toString() {
		return "{" + "\"signedBy\":\"" + signedBy + "\","
				+ "\"signedTime\":\"" + signedTime + "\","
				+ "\"validationTime\":\"" + validationTime + "\","
				+ "\"certificateSerialNumber\":\"" + certificateSerialNumber + "\","
				+ "\"signatureValid\":" + signatureValid + "}";
	}

}
