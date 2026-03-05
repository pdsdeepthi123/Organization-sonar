package com.dtt.organization.response.entity;

import java.io.Serializable;
import java.util.List;

import com.dtt.organization.request.entity.SignatureDeatils;

public class VerfiySignatureResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private List<SignatureDeatils> signatureDeatils;


	public List<SignatureDeatils> getSignatureDeatils() {
		return signatureDeatils;
	}

	public void setSignatureDeatils(List<SignatureDeatils> signatureDeatils) {
		this.signatureDeatils = signatureDeatils;
	}

	@Override
	public String toString() {
		return "{"+"\"signatureVerificationDetails\":" + signatureDeatils + "}";
	}

}
