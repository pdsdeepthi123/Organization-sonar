package com.dtt.organization.dto;

import java.io.Serializable;
import java.util.List;



public class TrustedStakeholderRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<TrustedStakeholderDto> trustedStakeholderDtosList;

	public List<TrustedStakeholderDto> getTrustedStakeholderDtosList() {
		return trustedStakeholderDtosList;
	}

	public void setTrustedStakeholderDtosList(List<TrustedStakeholderDto> trustedStakeholderDtosList) {
		this.trustedStakeholderDtosList = trustedStakeholderDtosList;
	}


	@Override
	public String toString() {
		return "TrustedStakeholderRequestDto{" + "trustedStakeholderDtosList=" + trustedStakeholderDtosList + '}';
	}


}
