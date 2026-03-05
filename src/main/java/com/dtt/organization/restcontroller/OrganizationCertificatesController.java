
package com.dtt.organization.restcontroller;

import java.util.Locale;

import com.dtt.organization.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dtt.organization.asserts.OrgnizationServiceAsserts;
import com.dtt.organization.constant.ApiEndpoints;
import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.OrganizationIssueCetificatesDto;
import com.dtt.organization.dto.RARequestDTO;
import com.dtt.organization.request.entity.GenerateSignature;
import com.dtt.organization.response.entity.APIResponse;
import com.dtt.organization.service.impl.OrganizationServiceCertficatesImpl;
import com.dtt.organization.util.Utility;

@RestController
public class OrganizationCertificatesController {

	private static final String CLASS = OrganizationCertificatesController.class.getSimpleName();
	Logger logger = LoggerFactory.getLogger(OrganizationCertificatesController.class);
	
	private final OrganizationServiceCertficatesImpl organizationServiceCertficatesImpl;
	
	private final MessageSource messageSource;
	
	public OrganizationCertificatesController(OrganizationServiceCertficatesImpl organizationServiceCertficatesImpl,
			MessageSource messageSource) {
		super();
		this.organizationServiceCertficatesImpl = organizationServiceCertficatesImpl;
		this.messageSource = messageSource;
	}

	/**
	 * Issue organization certificates.
	 */
	@PostMapping(value = ApiEndpoints.ISSUE_CERIFICATES_V1, produces = "application/json")
	public APIResponse issueOrganizationCertificates(@PathVariable String organizationUid, @PathVariable Boolean isPostPaid) {
	    String methodName = Utility.getMethodName();
	    logger.info("{} - {}: Issuing certificate for organization: {} with postPaid: {}", CLASS, methodName, organizationUid, isPostPaid);

	    try {
	        String response = organizationServiceCertficatesImpl.issueOrganizationCertificates(organizationUid, isPostPaid);
	        OrgnizationServiceAsserts.notNullorEmpty(response, Constant.ORGANIZATION_CERTIFICATE_NOT_ISSUED);

	        logger.info("{} - {}: Organization certificate issued successfully for orgUid: {}", CLASS, methodName, organizationUid);
	        return new APIResponse(true, messageSource.getMessage(Constant.API_RESPONSE_CERT_ISSUED_SUCCESFULLY, null, Locale.ENGLISH), response);

	    } catch (Exception e) {
	        logger.error("{} - {}: Error occurred while issuing certificate for orgUid: {}: {}", CLASS, methodName, organizationUid, e.getMessage(), e);
	        return new APIResponse(false, messageSource.getMessage(Constant.API_ERROR_SOMETHING_WENT_WRONG_PLEASE_TRY_AFTER_SOMETIME, null, Locale.ENGLISH), null);
	    }
	}

	
	// POST: Issue organization certificates
	@PostMapping(value = ApiEndpoints.ISSUE_CERIFICATES_V2, produces = "application/json")
	public APIResponse issueOrganizationCertificates(@RequestBody OrganizationIssueCetificatesDto organizationIssueCetificatesDto) {
	    String methodName = Utility.getMethodName();
	    logger.info("{} - {}: Issuing certificate for organization: {}", CLASS, methodName, organizationIssueCetificatesDto);

	    try {
	        String response = organizationServiceCertficatesImpl.issueOrganizationCertificatesNew(organizationIssueCetificatesDto);
	        OrgnizationServiceAsserts.notNullorEmpty(response, Constant.ESEAL_CERT_NOT_ISSUED);
	        logger.info("{} - {}: Certificate issued successfully for organization: {}", CLASS, methodName, organizationIssueCetificatesDto);
	        return new APIResponse(true, messageSource.getMessage(Constant.API_RESPONSE_ESEAL_CERT_ISSUED_SUCCESFULLY, null, Locale.ENGLISH), response);
	    } catch (Exception e) {
	        logger.error("{} - {}: Error occurred while issuing certificate for organization: {}: {}", CLASS, methodName, organizationIssueCetificatesDto, e.getMessage(), e);
	        return new APIResponse(false, e.getMessage(), null);
	    }
	}

	// POST: Issue wallet organization certificates
	@PostMapping(value = ApiEndpoints.ISSUE_WALLET_CERIFICATES, produces = "application/json")
	public APIResponse issueWalletOrganizationCertificates(@RequestBody OrganizationIssueCetificatesDto organizationIssueCetificatesDto) {
	    String methodName = Utility.getMethodName();
	    logger.info("{} - {}: Issuing wallet certificate for organization: {}", CLASS, methodName, organizationIssueCetificatesDto);

	    try {
	        String response = organizationServiceCertficatesImpl.issueWalletOrganizationCertificates(organizationIssueCetificatesDto);
	        OrgnizationServiceAsserts.notNullorEmpty(response, Constant.WALLET_CERTIFICATE_NOT_ISSUED);
	        logger.info("{} - {}: Wallet certificate issued successfully for organization: {}", CLASS, methodName, organizationIssueCetificatesDto);
	        return new APIResponse(true, messageSource.getMessage(Constant.API_RESPONSE_WALLET_CERT_ISSUED_SUCCESFULLY, null, Locale.ENGLISH), response);
	    } catch (Exception e) {
	        logger.error("{} - {}: Error occurred while issuing wallet certificate for organization: {}: {}", CLASS, methodName, organizationIssueCetificatesDto, e.getMessage(), e);
	        return new APIResponse(false, e.getMessage(), null);
	    }
	}





	// POST: Revoke certificate
	@PostMapping(value = ApiEndpoints.REVOKE_CERIFICATES, produces = "application/json")
	public APIResponse revokeCertificate(@RequestBody RARequestDTO requestBody) {
	    String methodName = Utility.getMethodName();
	    logger.info("{} - {}: Request to revoke certificate: {}", CLASS, methodName, requestBody);

	    try {
	        String response = organizationServiceCertficatesImpl.revokeCertificate(requestBody);
	        logger.info("{} - {}: Certificate revoked successfully for request: {}", CLASS, methodName, requestBody);
	        return new APIResponse(true, messageSource.getMessage(Constant.API_RESPONSE_CERTIFICATE_REVOKED_SUCCESSFULLY, null, Locale.ENGLISH), response);
	    } catch (Exception e) {
	        logger.error("{} - {}: Error occurred while revoking certificate for request: {}: {}", CLASS, methodName, requestBody, e.getMessage(), e);
	        return new APIResponse(false, messageSource.getMessage(Constant.API_ERROR_SOMETHING_WENT_WRONG_PLEASE_TRY_AFTER_SOMETIME, null, Locale.ENGLISH), null);
	    }
	}

	// GET: Check certificate status for eSeal
	@GetMapping(value = ApiEndpoints.GET_CERIFICATES_STATUS)
	public String checkCertificateStatusForEseal() {
	    String methodName = Utility.getMethodName();
	    logger.info("{} - {}: Checking certificate status for eSeal", CLASS, methodName);

	    try {
	        String status = organizationServiceCertficatesImpl.checkCertificateStatus();
	        logger.info("{} - {}: Certificate status fetched successfully: {}", CLASS, methodName, status);
	        return new APIResponse(true, "Certificate Status", "\"" + status + "\"").toString();
	    } catch (Exception e) {
	        logger.error("{} - {}: Error occurred while checking certificate status: {}", CLASS, methodName, e.getMessage(), e);
	        return new APIResponse(false, messageSource.getMessage(Constant.API_ERROR_SOMETHING_WENT_WRONG_PLEASE_TRY_AFTER_SOMETIME, null, Locale.ENGLISH), null).toString();
	    }
	}

	// POST: Generate signature
	@PostMapping(value = ApiEndpoints.GENERATE_SIGNATURE_ORG, consumes = "application/json")
	public APIResponse generateSignature(@RequestBody GenerateSignature generateSignatureRequest) {
	    String methodName = Utility.getMethodName();
	    logger.info("{} - {}: Generating signature for request: {}", CLASS, methodName, generateSignatureRequest);

	    try {
	        String response = organizationServiceCertficatesImpl.generateSignature(generateSignatureRequest);
	        logger.info("{} - {}: Signature generated successfully for request: {}", CLASS, methodName, generateSignatureRequest);
	        return new APIResponse(true, messageSource.getMessage(Constant.API_RESPONSE_GENERATE_SIGNATURE_RESPONSE, null, Locale.ENGLISH), response);
	    } catch (Exception e) {
	        logger.error("{} - {}: Error occurred while generating signature for request: {}: {}", CLASS, methodName, generateSignatureRequest, e.getMessage(), e);
	        return new APIResponse(false, messageSource.getMessage(Constant.API_ERROR_SOMETHING_WENT_WRONG_PLEASE_TRY_AFTER_SOMETIME, null, Locale.ENGLISH), null);
	    }
	}

	// POST: Email SPOC for eSeal certificate generated successfully
	@PostMapping(ApiEndpoints.SEND_EMAIL_TO_SPOC_ESEAL_CERIFICATES_GENRATED)
	public ApiResponses sendEmail(@RequestParam String orgId) {
		 String methodName = Utility.getMethodName();
		 logger.info("{} - {} - {}: Send email to spoc using orgID:", CLASS, methodName,orgId);
		return organizationServiceCertficatesImpl.sendEmailEsealCertificateGenerated(orgId);
	}

	// GET: Get all organizations and certificates
	@GetMapping(ApiEndpoints.GET_ALL_ORG_CERT)
	public ApiResponses getAllOrganizationsAndCert(@RequestParam String orgId){
		String methodName = Utility.getMethodName();
		logger.info("{} - {} - {}: get all organization cert using orgID:", CLASS, methodName,orgId);
		return organizationServiceCertficatesImpl.getAllOrganizationsAndCert(orgId);
	}


	@GetMapping(ApiEndpoints.GET_WALLET_CERT_BY_OUID)
	public ApiResponses getWalletCertByOuid(@PathVariable String ouid){
		return organizationServiceCertficatesImpl.getWalletCertByOuid(ouid);
	}


	@PostMapping(value = ApiEndpoints.DUPLICATE_TRANNSACTION_REFERENCE)
	public APIResponse findDuplicatereferenceid(@RequestBody OrganizationIssueCetificatesDto dto) {
		try {
			organizationServiceCertficatesImpl.checkDuplicateTransactionId(dto);
			return new APIResponse(true,messageSource.getMessage(Constant.API_RESPONSE_TRANNACTION_REFERENCE_ID,null,Locale.ENGLISH),"VALID" );
		} catch (Exception e) {
			return new APIResponse(false, e.getMessage(), null);
		}
	}


}
