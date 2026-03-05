package com.dtt.organization.dto;

import java.io.Serializable;

import com.dtt.organization.model.TrustedStakeholder;

public class EmailReqDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String emailOtp;
	
	private int ttl;
	
	private String emailId;

	private boolean org;
	
	private boolean eseal;
	
	private EsealCertificateDto esealCertificateDto;
	
	private String link;
	
	private TrustedStakeholder trustedStakeholder;

	public boolean isOrg() {
		return org;
	}

	public void setOrg(boolean org) {
		this.org = org;
	}

	public String getEmailOtp() {
		return emailOtp;
	}

	public void setEmailOtp(String emailOtp) {
		this.emailOtp = emailOtp;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	

	public boolean isEseal() {
		return eseal;
	}

	public void setEseal(boolean eseal) {
		this.eseal = eseal;
	}

	public EsealCertificateDto getEsealCertificateDto() {
		return esealCertificateDto;
	}

	public void setEsealCertificateDto(EsealCertificateDto esealCertificateDto) {
		this.esealCertificateDto = esealCertificateDto;
	}
	
	

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	

	public TrustedStakeholder getTrustedStakeholder() {
		return trustedStakeholder;
	}

	public void setTrustedStakeholder(TrustedStakeholder trustedStakeholder) {
		this.trustedStakeholder = trustedStakeholder;
	}

	@Override
	public String toString() {
		return "EmailReqDto{" +
				"emailOtp='" + emailOtp + '\'' +
				", ttl=" + ttl + '\''+
				", emailId='" + emailId + '\'' +
				", org=" + org + '\''+
				", eseal=" + eseal + '\''+
				", esealCertificateDto=" + esealCertificateDto + '\''+
				", link=" + link + '\''+
				", trustedStakeholder=" + trustedStakeholder +
				'}';
	}

}
