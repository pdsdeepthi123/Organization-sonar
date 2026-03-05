package com.dtt.organization.dto;
public class BenificiariesRespDto {

	/**
	 * 
	 */


	BenificiariesResponseDto benificiariesResponseDtos;

	public BenificiariesResponseDto getBenificiariesResponseDtos() {
		return benificiariesResponseDtos;
	}

	public void setBenificiariesResponseDtos(BenificiariesResponseDto benificiariesResponseDtos) {
		this.benificiariesResponseDtos = benificiariesResponseDtos;
	}

	@Override
	public String toString() {
		return "BenificiariesRespDto [benificiariesResponseDtos=" + benificiariesResponseDtos + "]";
	}
}
