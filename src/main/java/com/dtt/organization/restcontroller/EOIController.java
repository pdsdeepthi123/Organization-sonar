package com.dtt.organization.restcontroller;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.RegisterOrganizationDTO;
import com.dtt.organization.dto.TrustedStakeholderDto;
import com.dtt.organization.dto.TrustedStakeholderRequestDto;
import com.dtt.organization.service.iface.EOIIface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EOIController {
    public static final String CLASS = "EOIController";
    Logger logger = LoggerFactory.getLogger(EOIController.class);

    private final EOIIface eoiIface;

    // Constructor Injection
    public EOIController(EOIIface eoiIface) {
        this.eoiIface = eoiIface;
    }

    @PostMapping("/api/post/service/register/organization/eoi/{referenceId}")
    public ApiResponses registerOrganizationEOIPortal(@RequestBody RegisterOrganizationDTO registerOrganizationDTO, @PathVariable String referenceId) {
        logger.info(CLASS + "registerOrganization orgname {}",registerOrganizationDTO.getOrganizationName());
        logger.info(CLASS + "registerOrganization DTO request {}", registerOrganizationDTO);
        return eoiIface.registerTrustedOrganizationEOIPortal(registerOrganizationDTO,referenceId);

    }

   //Register the organization without reference id
	@PostMapping("/api/post/service/register/organization/eoi")
	public ApiResponses onboardOrganization(@RequestBody RegisterOrganizationDTO registerOrganizationDTO) {
		return eoiIface.registerTrustedOrganizationEOI(registerOrganizationDTO);
	}
	
	//API for add all stake holder from admin portal(import)
	@PostMapping("/api/post/addstakeholderlist")
	public ApiResponses addStakeHolders(@RequestBody TrustedStakeholderRequestDto trustedStakeholderRequestDto) {
	    return eoiIface.addStakeHoldersList(trustedStakeholderRequestDto);
    }
	
	//update record of stakeholder
	@PostMapping("/api/update/stakeholder")
    public ApiResponses updateStakeHolder(@RequestBody TrustedStakeholderDto trustedStakeHolder) {
    	return eoiIface.updateStakeHolder(trustedStakeHolder);
    }
	

	@GetMapping("/api/get/stakeholder/{referenceId}")
	public ApiResponses fetchStakeHolder(@PathVariable String referenceId) {
	    return eoiIface.getStakeHolder(referenceId);
	}
	
	//get All stakeholder list for eoi
	@GetMapping(value = "/api/get/allstakeholder")
    public ApiResponses getAllStakeHolder(@RequestParam String referredBy, @RequestParam String stakeholderType) {
    	return eoiIface.getAllStakeHolder(referredBy,stakeholderType);
    }
    
    //get stakeholder list for eoi
    @GetMapping("/api/get-stakeholders-list/{spocEmail}")
    public ApiResponses getStakeHoldersListEOI(@PathVariable String spocEmail) {
    	return eoiIface.getStakeHolderList(spocEmail);
    }
    
    //send email otp to spoc
    @PostMapping("/api/post/sendemailotp/{referenceId}")
    public ApiResponses sendEmailOTP(@PathVariable String referenceId) {
        return eoiIface.sendEmailOTP(referenceId);
    }
    
    //send invitation link to spoc
    @PostMapping("/api/initationlink/spoc/{email}")
    public ApiResponses invitationLinkToSpoc(@PathVariable("email") String spocemail) {
        return eoiIface.sendEmailToSpoc(spocemail);
    }
}
