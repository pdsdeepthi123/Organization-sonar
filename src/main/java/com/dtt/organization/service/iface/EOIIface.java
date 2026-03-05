package com.dtt.organization.service.iface;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.RegisterOrganizationDTO;
import com.dtt.organization.dto.TrustedStakeholderDto;
import com.dtt.organization.dto.TrustedStakeholderRequestDto;

public interface EOIIface {

	ApiResponses registerTrustedOrganizationEOIPortal(RegisterOrganizationDTO registerOrganizationDTO, String referenceId);

    ApiResponses sendEmailOTP(String referenceId);

    ApiResponses registerTrustedOrganizationEOI(RegisterOrganizationDTO registerOrganizationDTO);
    
    ApiResponses addStakeHoldersList(TrustedStakeholderRequestDto trustedStakeholderRequestDto);
    
    ApiResponses getAllStakeHolder(String referredBy, String stakeholderType);
    
    ApiResponses getStakeHolder(String referenceId);
    
    ApiResponses sendEmailToSpoc(String spocEmail);
    
    ApiResponses updateStakeHolder(TrustedStakeholderDto trustedStakeHolder);
    
    ApiResponses getStakeHolderList(String spocEmail);
}
