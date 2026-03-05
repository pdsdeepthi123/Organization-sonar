package com.dtt.organization.request.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class SignatureVerificationContext1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The document type. */
	private String documentType;

	/** The subscriber uid. */
	private String subscriberUid;

	@NotBlank(message = "Organization UID is required and cannot be blank.")
	private String organizationUid;

	@NotEmpty(message = "At least one signatory must be provided.")
	private List<String> signatories;

	/** The signature. */
	private String signature;

	/** The doc data. */

	@NotBlank(message = "DocData cannot be null or empty")
	private String docData;

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getSubscriberUid() {
		return subscriberUid;
	}

	public void setSubscriberUid(String subscriberUid) {
		this.subscriberUid = subscriberUid;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getDocData() {
		return docData;
	}

	public void setDocData(String docData) {
		this.docData = docData;
	}


	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	public List<String> getSignatories() {
		return signatories;
	}

	public void setSignatories(List<String> signatories) {
		this.signatories = signatories;
	}

	@Override
	public String toString() {
		return "{" + "\"signature\":\"" + signature + "\"," + "\"docData\":\"" + docData + "\","
				+ "\"organizationUid\":" + organizationUid + "\"," + "\"signatories\":" + signatories + "}";
	}

}
