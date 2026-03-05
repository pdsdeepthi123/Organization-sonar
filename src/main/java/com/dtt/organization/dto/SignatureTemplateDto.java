package com.dtt.organization.dto;

import java.io.Serializable;

public class SignatureTemplateDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int signatureTemplateId;
	
	private int esealSignatureTemplateId;
	
	private String designation;
	
	private String handWrittenSignature;
	
	private String companyName;
	
	private String fullName;
	
	private String esealImage;
	
	private String selfieThumbnail;

	public int getSignatureTemplateId() {
		return signatureTemplateId;
	}

	public void setSignatureTemplateId(int signatureTemplateId) {
		this.signatureTemplateId = signatureTemplateId;
	}

	public int getEsealSignatureTemplateId() {
		return esealSignatureTemplateId;
	}

	public void setEsealSignatureTemplateId(int esealSignatureTemplateId) {
		this.esealSignatureTemplateId = esealSignatureTemplateId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getHandWrittenSignature() {
		return handWrittenSignature;
	}

	public void setHandWrittenSignature(String handWrittenSignature) {
		this.handWrittenSignature = handWrittenSignature;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	
	public String getEsealImage() {
		return esealImage;
	}

	public void setEsealImage(String esealImage) {
		this.esealImage = esealImage;
	}

	
	public String getSelfieThumbnail() {
		return selfieThumbnail;
	}

	public void setSelfieThumbnail(String selfieThumbnail) {
		this.selfieThumbnail = selfieThumbnail;
	}

	@Override
	public String toString() {
		return "SignatureTemplateDto [signatureTemplateId=" + signatureTemplateId + ", esealSignatureTemplateId="
				+ esealSignatureTemplateId + ", designation=" + designation + ", handWrittenSignature="
				+ handWrittenSignature + ", companyName=" + companyName + ", fullName=" + fullName + ", esealImage="
				+ esealImage + ", selfieThumbnail=" + selfieThumbnail + "]";
	}

}
