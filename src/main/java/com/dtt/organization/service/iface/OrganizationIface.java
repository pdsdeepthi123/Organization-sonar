package com.dtt.organization.service.iface;



import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.*;
import com.dtt.organization.exception.OrgnizationServiceException;
import com.dtt.organization.request.entity.SignatureVerificationContext1;

import java.util.List;

public interface OrganizationIface {

	ApiResponses registerOrganization(RegisterOrganizationDTO registerOrganizationDTO);
	
	ApiResponses updateOrganization(RegisterOrganizationDTO updateOrganizationDTO);
	
	ApiResponses getOrganizationDetailsById(String organizationUid);
	
	ApiResponses getOrgDetailsByOrganizationName(String organizationName);
	
	ApiResponses getOrganizationBySerachType(String organizationName);
	
	ApiResponses isOrganizationAlreadyExixts(String organizationName);
	
	ApiResponses addOrganizationEmails(EmailListDto emailList);
	
	ApiResponses getSubascriberEmailBySearchType(String searchType);
	
	
	ApiResponses getOrganizationListAndUid(String suid);
		
	ApiResponses getOrganizationPrepetryStatus(String emailId);
	
	ApiResponses getOrganizationPrepetryStatusByOrgId(String orgId);
	
	ApiResponses getAgentUrlByOrg(String orgId);
	
	ApiResponses getOrgListAndUid();

	ApiResponses getOrganizationListForSearch();
	
	ApiResponses getSigntoryList(String organizationUid);
	
	ApiResponses getSigntoryListByOrgId(String organizationUid);

	ApiResponses verifySignedDocumnet(SignatureVerificationContext1 signatureVerificationContext1);
	
	ApiResponses getAllTemplates();
	ApiResponses getAllTemplatesWithoutImages();
	ApiResponses getTemplateImage(int id);
	
	SignatureTemplateDto getTemplatesByTempId(GetTemplateDto getTemplateDto) throws OrgnizationServiceException;
	
	ApiResponses getUserTemplateDetails(GetTemplateDto getTemplateDto);
	ApiResponses getOrganizationListBySuid(String suid);

	ApiResponses linkEmail(OrgUser orgUser);

	ApiResponses sendOtp(EmailReqDto otpDto);

	ApiResponses deactive(DeactivateDto deactivateDto);

	ApiResponses orgStatus(String orgUid);
	
	ApiResponses getCertificateDetails(String orgUid);
	
	ApiResponses getCertificateStatusList(OrganizationIdDto orgUidList);

	ApiResponses checkValidCertificateSerialNumber(String certificateSerialNumber);


	ApiResponses sendEmail(String spocEmail, String orgName);


	ApiResponses toggleManageByAdmin(ToggleManageByAdmin toggleManageByAdmin);


	ApiResponses checkTemplates(List<CheckTemplateDto> checkTemplateDtoList);


	ApiResponses getOrganizationStatus();
	
}
