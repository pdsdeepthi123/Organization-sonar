package com.dtt.organization.restcontroller;

import com.dtt.organization.constant.Constant;
import com.dtt.organization.service.impl.OrganizationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.dtt.organization.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.dtt.organization.constant.ApiEndpoints;
import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.exception.OrgnizationServiceException;
import com.dtt.organization.request.entity.SignatureVerificationContext1;
import com.dtt.organization.service.iface.OrganizationIface;
import com.dtt.organization.util.AppUtil;
import com.dtt.organization.util.Utility;
import com.dtt.organization.util.ValidationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ug.daes.DAESService;
import ug.daes.PKICoreServiceException;
import ug.daes.Result;

import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(ApiEndpoints.ORGANIZATION_BASE)
public class OrganizationController {

    private static final String CLASS = OrganizationController.class.getSimpleName();
    Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    private OrganizationIface organizationIface;

    private OrganizationServiceImpl organizationServiceImpl;


    public OrganizationController(OrganizationIface organizationIface,
                                  OrganizationServiceImpl organizationServiceImpl) {
        super();
        this.organizationIface = organizationIface;
        this.organizationServiceImpl = organizationServiceImpl;
    }


    @GetMapping(ApiEndpoints.GET_SERVICE_STATUS)
    public ApiResponses getServiceStatus() {
        try {
            logger.info("{} - {} : Organization Service is running.", CLASS, Utility.getMethodName());
            return AppUtil.createApiResponse(true, Constant.ORG_SERVICE_IS_RUNNING, null);
        } catch (Exception e) {
            logger.error("{} - {} : Exception occurred >> Organization Service is not running. Error: {}", CLASS,
                    Utility.getMethodName(), e.getMessage(), e);
            return AppUtil.createApiResponse(false, Constant.ERROR_SERVICE_UNAVAILABLE, null);
        }
    }

    /**
     * Register organization.
     */
    @PostMapping(ApiEndpoints.REGISTER_ORGANIZATION)
    public ApiResponses registerOrganization(@Valid @RequestBody RegisterOrganizationDTO registerOrganizationDTO)  {


        logger.info("{} - {} : Registering organization with name: {}", CLASS, Utility.getMethodName(),
                registerOrganizationDTO.getOrganizationName());
        return organizationIface.registerOrganization(registerOrganizationDTO);
    }

    @PostMapping(ApiEndpoints.UPDATE_ORGANIZATION)
    public ApiResponses updateOrgnization(@RequestBody RegisterOrganizationDTO updateOrganizationDTO)  {
        logger.info("{} - {} : Updating organization with ID: {}", CLASS, Utility.getMethodName(),
                updateOrganizationDTO.getOrganizationId());
        return organizationIface.updateOrganization(updateOrganizationDTO);
    }

    @GetMapping(ApiEndpoints.GET_ORGANIZATION_DETAILS_BY_ID)
    public ApiResponses getOrganizationDetails(@PathVariable String organizationUid) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching organization details by ID: {}", CLASS, methodName, organizationUid);
        return organizationIface.getOrganizationDetailsById(organizationUid);
    }

    @GetMapping(ApiEndpoints.GET_ORGANIZATION_DETAILS_BY_NAME)
    public ApiResponses getOrgDetailsByOrganizationName(@RequestParam String organizationName) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching organization details by name: {}", CLASS, methodName, organizationName);
        return organizationIface.getOrgDetailsByOrganizationName(organizationName);
    }

    @GetMapping(ApiEndpoints.IS_ORGANIZATION_EXIST)
    public ApiResponses isOrganizationExist(@RequestParam String organizationName) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Checking if organization exists: {}", CLASS, methodName, organizationName);
        return organizationIface.isOrganizationAlreadyExixts(organizationName);
    }

    @PostMapping(ApiEndpoints.ADD_AUTHORIZED_USER)
    public ApiResponses addOrganizationEmails(@RequestBody EmailListDto emailList) throws JsonProcessingException {
        String methodName = Utility.getMethodName();
        String dtoAsJson = Utility.convertToJson(emailList);
        logger.info("{} - {} : Adding authorized emails: {}", CLASS, methodName, dtoAsJson);
        return organizationIface.addOrganizationEmails(emailList);
    }

    @GetMapping(ApiEndpoints.GET_SUBSCRIBER_EMAIL_BY_SEARCH_TYPE)
    public ApiResponses getSubascriberEmailBySearchType(@RequestParam String searchType) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching subscriber emails by search type: {}", CLASS, methodName, searchType);
        return organizationIface.getSubascriberEmailBySearchType(searchType);
    }

    @GetMapping(ApiEndpoints.GET_ORG_LIST)
    public ApiResponses getOrganizationListAndUid(@RequestParam String email) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching organization list by email: {}", CLASS, methodName, email);
        return organizationIface.getOrganizationListAndUid(email);
    }

    @GetMapping(ApiEndpoints.GET_PREPARATORY_STATUS)
    public ApiResponses getOrganizationPrepetryStatus(@RequestParam String email) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching property status by email: {}", CLASS, methodName, email);
        return organizationIface.getOrganizationPrepetryStatus(email);
    }

    @GetMapping(ApiEndpoints.GET_BULKSIGNER_LIST)
    public ApiResponses getOrganizationPrepetryStatusByOrgId(@RequestParam String orgId) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching property status by org ID: {}", CLASS, methodName, orgId);
        return organizationIface.getOrganizationPrepetryStatusByOrgId(orgId);
    }

    @GetMapping(ApiEndpoints.GET_AGENT_URL)
    public ApiResponses getAgentUrl(@RequestParam String orgId) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching agent URL by org ID: {}", CLASS, methodName, orgId);
        return organizationIface.getAgentUrlByOrg(orgId);
    }

    @GetMapping(ApiEndpoints.GET_ORGANIZATION)
    public ApiResponses getOrganizationListWithUid() {

        return organizationIface.getOrgListAndUid();
    }

    @GetMapping(ApiEndpoints.GET_SIGNTORY_LIST)
    public ApiResponses getSigntoryListByOrgId(@PathVariable String organizationUid) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching signatory list by org UID: {}", CLASS, methodName, organizationUid);
        return organizationIface.getSigntoryList(organizationUid);
    }

    @GetMapping(ApiEndpoints.GET_SIGNTORY_LIST_BY_ORGID)
    public ApiResponses getSigntoryListByOrganizationId(@PathVariable String organizationUid) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching signatory list by organization ID: {}", CLASS, methodName, organizationUid);
        return organizationIface.getSigntoryListByOrgId(organizationUid);
    }

    @PostMapping(ApiEndpoints.VERIFY_SIGNED_DOCUMENT)
    public ApiResponses verfiySignedDocument(@RequestBody SignatureVerificationContext1 signatureVerificationContext) throws JsonProcessingException {
        String methodName = Utility.getMethodName();
        String dtoAsJson = Utility.convertToJson(signatureVerificationContext);
        logger.info("{} - {} : Verifying signed document for organization UID: {}", CLASS, methodName, dtoAsJson);
        return organizationServiceImpl.verifySignedDocumnet(signatureVerificationContext);
    }

    @GetMapping(ApiEndpoints.GET_ALL_TEMPLATES)
    public ApiResponses getAllTemplates() {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching all templates", CLASS, methodName);
        return organizationIface.getAllTemplates();
    }

    @GetMapping(ApiEndpoints.GET_ALL_TEMPLATES_WITHOUT_IMAGES)
    public ApiResponses getAllTemplatesWithoutImages() {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching all templates without images", CLASS, methodName);
        return organizationIface.getAllTemplatesWithoutImages();
    }


    @GetMapping(ApiEndpoints.GET_TEMPLATE_IMAGE)
    public ApiResponses getTemplateImage(@RequestParam int id) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching all templates", CLASS, methodName);
        return organizationIface.getTemplateImage(id);
    }

    @PostMapping(ApiEndpoints.GET_TEMPLATES_BY_DTO)
    public SignatureTemplateDto getTemplates(@RequestBody GetTemplateDto getTemplateDto) throws JsonProcessingException, OrgnizationServiceException {
        String methodName = Utility.getMethodName();
        String dtoAsJson = Utility.convertToJson(getTemplateDto);
        logger.info("{} - {} : Fetching templates with request: {}", CLASS, methodName, dtoAsJson);
        return organizationIface.getTemplatesByTempId(getTemplateDto);
    }

    @PostMapping(ApiEndpoints.GET_USER_TEMPLATE_DETAILS)
    public ApiResponses getUserTemplateDetails(@RequestBody GetTemplateDto getTemplateDto) throws JsonProcessingException, OrgnizationServiceException {
        String methodName = Utility.getMethodName();
        String dtoAsJson = Utility.convertToJson(getTemplateDto);

        String result = ValidationUtil.validate(getTemplateDto);
        if (result != null) {
            throw new OrgnizationServiceException(result);
        }
        logger.info("{} - {} : Fetching user template details with requesct: {}", CLASS, methodName, dtoAsJson);
        return organizationIface.getUserTemplateDetails(getTemplateDto);
    }

    @GetMapping(ApiEndpoints.GET_ORG_LIST_BY_SUID)
    public ApiResponses getOrgList(@RequestParam String suid, HttpServletRequest httpServletRequest) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching organization list by SUID: {}", CLASS, methodName, suid);
        return organizationIface.getOrganizationListBySuid(suid);
    }

    @PostMapping(ApiEndpoints.LINK_UGPASS_EMAIL)
    public ApiResponses linkEmail(@RequestBody OrgUser orgUser, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String methodName = Utility.getMethodName();
        String dtoAsJson = Utility.convertToJson(orgUser);
        logger.info("{} - {} : Linking email with request: {}", CLASS, methodName, dtoAsJson);
        return organizationIface.linkEmail(orgUser);
    }

    @PostMapping(ApiEndpoints.SEND_OTPS)
    public ApiResponses sendOtp(@RequestBody EmailReqDto otpDto, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String methodName = Utility.getMethodName();
        String dtoAsJson = Utility.convertToJson(otpDto);
        logger.info("{} - {} : Sending OTP with request: {}", CLASS, methodName, dtoAsJson);
        return organizationIface.sendOtp(otpDto);
    }

    @PostMapping(ApiEndpoints.DEACTIVE_BUSINESS_USER)
    public ApiResponses deactive(@RequestBody DeactivateDto deactivateDto) throws JsonProcessingException {
        String methodName = Utility.getMethodName();
        String dtoAsJson = Utility.convertToJson(deactivateDto);
        logger.info("{} - {} : Deactivating with request: {}", CLASS, methodName, dtoAsJson);
        return organizationIface.deactive(deactivateDto);
    }

    @GetMapping(ApiEndpoints.GET_ORG_STATUS)
    public ApiResponses orgStatus(@RequestParam String orgUid) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching organization status with org UID: {}", CLASS, methodName, orgUid);
        return organizationIface.orgStatus(orgUid);
    }

    @GetMapping(ApiEndpoints.GET_CERTFICATE_DETAILS)
    public ApiResponses getCertificateDetails(@RequestParam String orgUid) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching certificate details for org UID: {}", CLASS, methodName, orgUid);
        return this.organizationIface.getCertificateDetails(orgUid);
    }

    @PostMapping(ApiEndpoints.GET_ORG_LIST)
    public ApiResponses getOrgCertificateList(@RequestBody OrganizationIdDto orgUidList) throws JsonProcessingException {
        String methodName = Utility.getMethodName();
        String dtoAsJson = Utility.convertToJson(orgUidList);
        logger.info("{} - {} : Fetching organization certificate list with request: {}", CLASS, methodName, dtoAsJson);
        return organizationIface.getCertificateStatusList(orgUidList);
    }

    @PostMapping(ApiEndpoints.GET_SUBSCRIBER_CERT_BY_CERT_SERIAL_NUMBER)
    public ApiResponses checkValidCertificateSerialNumber(
            @PathVariable("certificateSerialNumber") String certificateSerialNumber) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Checking valid certificate serial number: {}", CLASS, methodName,
                certificateSerialNumber);
        return organizationIface.checkValidCertificateSerialNumber(certificateSerialNumber);
    }

    /*
     * send email to spoc
     */
    @PostMapping(ApiEndpoints.SEND_EMAIL_SPOC_ORG_NAME)
    public ApiResponses sendEmail(@PathVariable("spocemail") String spocEmail, @PathVariable("orgName") String orgName) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Sending email to SPOC with email: {} and organization name: {}", CLASS, methodName,
                spocEmail, orgName);
        return organizationIface.sendEmail(spocEmail, orgName);
    }

    @PostMapping(ApiEndpoints.GET_TEMPLATES_ENCRYPT)
    public String getTemplatesEncrypt(@RequestBody String request)
            throws PKICoreServiceException, JsonProcessingException, OrgnizationServiceException {
        String methodName = Utility.getMethodName(); // Get the method name dynamically
        ObjectMapper mapper = new ObjectMapper();
        // Decrypt the data
        Result result = DAESService.decryptSecureWireData(request);
        String decryptedString = new String(result.getResponse());
        GetTemplateDto getTemplateDto = mapper.readValue(decryptedString, GetTemplateDto.class);
        logger.info("{} - {} : Received decrypted data for templates: {}", CLASS, methodName, getTemplateDto);
        // Get the template based on the provided GetTemplateDto
        SignatureTemplateDto signatureTemplateDto = organizationIface.getTemplatesByTempId(getTemplateDto);
        // Convert signatureTemplateDto to string
        String signatureTemplateDtoString = mapper.writeValueAsString(signatureTemplateDto);
        // Encrypt the response data
        Result encrypt = DAESService.createSecureWireData(signatureTemplateDtoString);
        // Return the encrypted data
        logger.info("{} - {} : Returning encrypted signature template data", CLASS, methodName);
        return new String(encrypt.getResponse());
    }


    @GetMapping(ApiEndpoints.GET_ALL_ORG_LIST)
    public ApiResponses getAllOrg(@RequestParam("searchType") String searchType) {
        // Both CLASS and searchType are now passed as arguments to the format specifiers
        logger.info("{} getOrganizationBySearchType {}", CLASS, searchType);
        return organizationServiceImpl.getOrganizationBySerachType(searchType);
    }

    @GetMapping(ApiEndpoints.GET_ORG_LIST_FOR_SEARCH)
    public ApiResponses getOrganizationListForSearch() {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching organization list with UID", CLASS, methodName);
        return organizationIface.getOrganizationListForSearch();
    }


    @PostMapping(ApiEndpoints.TOGGLE_MANAGE_BY_ADMIN)
    public ApiResponses toggleManageByAdmin(@RequestBody ToggleManageByAdmin toggleManageByAdmin) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : Fetching organization list with UID", CLASS, methodName);
        return organizationIface.toggleManageByAdmin(toggleManageByAdmin);
    }

    @PostMapping(ApiEndpoints.CHECK_TEMPLATES)
    public ApiResponses checkTemplates(@RequestBody List<CheckTemplateDto> templateList) {
        String methodName = Utility.getMethodName();
        logger.info("{} - {} : checkTemplates organization list ", CLASS, methodName);
        return organizationIface.checkTemplates(templateList);
    }

    @GetMapping(ApiEndpoints.GET_COUNT)
    public ApiResponses getOrganizationStats() {
        return organizationServiceImpl.getOrganizationStatus();
    }




}
