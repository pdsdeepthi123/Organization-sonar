package com.dtt.organization.service.iface;

import com.dtt.organization.constant.ApiResponses;

import com.dtt.organization.dto.BenificiariesDto;

import java.util.List;


public interface BeneficiaryIface {
	
	
	ApiResponses verifyByEgpForVendor(String vendorId, String orgid);

    ApiResponses addBeneficiary(BenificiariesDto benificiariesDto);

    ApiResponses findPrivilegeByStatus();

    ApiResponses getAllBeneficiaries();

    ApiResponses getAllBeneficiariesBySponsor(String sponsorId);

    ApiResponses getBeneficiaryById(int id);

    ApiResponses updateBeneficiary(BenificiariesDto benificiariesDto);

    ApiResponses dlink(int id);

    ApiResponses verifyOnBoardingSponsor(String suid);

    ApiResponses linkSponsor(String beneficiaryDigitalId, int id);
    
    ApiResponses linkAllSponsor(BenificiariesDto benificiariesDto);

    ApiResponses getAllSponsersBySuid(String suid);

    ApiResponses changeStatusForSSP(int id);

    ApiResponses addMultipleBeneficiaries(List<BenificiariesDto> multipleBenificiariesDto);

    ApiResponses getVendorsByVendorId(String vendorId);


}



